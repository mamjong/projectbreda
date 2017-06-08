package nl.gemeente.breda.bredaapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.vision.text.Text;

import nl.gemeente.breda.bredaapp.domain.Report;
import nl.gemeente.breda.bredaapp.util.ReverseGeocoder;

import static nl.gemeente.breda.bredaapp.fragment.MainScreenListFragment.EXTRA_REPORT;

public class DetailedReportActivity extends AppBaseActivity {
	
	private static final String TAG = "DetailedReportActivity";
	private Button extraReport;
	private boolean isPressed;
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailed_report);
		super.setMenuSelected(getIntent().getExtras());
		
		TextView description = (TextView) findViewById(R.id.DetailedReportActivity_tv_kindOfDefectInput);
		ImageView mediaUrl = (ImageView) findViewById(R.id.DetailedReportActivity_iv_image);
		extraReport = (Button) findViewById(R.id.DetailedReportActivity_bt_extraReportBtn);
		TextView category = (TextView) findViewById(R.id.DetailedReportActivity_tv_categoryInput);
		progressBar = (ProgressBar) findViewById(R.id.DetailedReportActivity_pb_imageProgressBar);
		TextView count = (TextView) findViewById(R.id.DetailedReportActivity_tv_reportCounter); 
		
		Bundle extras = getIntent().getExtras();
		String getMediaUrl = extras.getString("MediaUrl");
		int getNoImage = extras.getInt("NoImage");
		
		
		final Report r = (Report) extras.getSerializable(EXTRA_REPORT);
		final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext(), null, null, 1);
		
		double lat = r.getLatitude();
		double lng = r.getLongitude();
		
		ReverseGeocoder geocoder = new ReverseGeocoder(lat, lng, this);
		TextView address = (TextView) findViewById(R.id.DetailedReportActivity_tv_address);
		String address_content = geocoder.getAddress();
		address_content = address_content.replaceAll("(?<=(^|\\G)\\S{0,100}\\s\\S{0,100})\\s", "\n");
		address.setText(address_content);
		
		
		category.setText(r.getServiceName());
		count.setText(r.getUpvotes() + "");
		
		description.setText(r.getDescription());
		
		progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#d91d49"), PorterDuff.Mode.SRC_ATOP);
		

		if (dbh.checkReport(r) == true) {
			extraReport.setBackgroundResource(R.drawable.onimage2);
			isPressed = true;
		} else if (dbh.checkReport(r) == false) {
			extraReport.setBackgroundResource(R.drawable.offimage);
			isPressed = false;
		}
		
		super.setShareText(getResources().getString(R.string.created_report_share_text_prefix).trim() + " " + r.getDescription());
		
		if(getMediaUrl == null){
		Glide.with(this)
				.load(getNoImage)
				.listener(new RequestListener<Drawable>() {
					@Override
					public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
						progressBar.setVisibility(View.GONE);
						return false;
					}
					
					@Override
					public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
						progressBar.setVisibility(View.GONE);
						return false;
					}
				})
				.into(mediaUrl);
		} else {
			Glide.with(this)
					.load(getMediaUrl)
					.listener(new RequestListener<Drawable>() {
						@Override
						public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
							progressBar.setVisibility(View.GONE);
							return false;
						}
						
						@Override
						public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
							progressBar.setVisibility(View.GONE);
							return false;
						}
					})
					.into(mediaUrl);
		}
		
		mediaUrl.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(TAG, "onClick geactiveerd.");
				Intent fullscreenImageIntent = new Intent(getApplicationContext(), DetailedReportActivityImage.class);
				fullscreenImageIntent.putExtra(EXTRA_REPORT, r);
				startActivity(fullscreenImageIntent);
			}
			
		});
		
		extraReport.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if (isPressed == false) {
					dbh.addReport(r);
					extraReport.setBackgroundResource(R.drawable.onimage2);
					Log.i(TAG, "melding = checked");
					isPressed = true;
					
				} else if (isPressed == true) {
					dbh.deleteReport(r);
					extraReport.setBackgroundResource(R.drawable.offimage);
					Log.i(TAG, "melding = unchecked");
					isPressed = false;
				}
			}
		});
	}
}
