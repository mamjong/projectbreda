package nl.gemeente.breda.bredaapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.media.MediaRouteProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import nl.gemeente.breda.bredaapp.api.ImageLoader;
import nl.gemeente.breda.bredaapp.domain.Report;
import static nl.gemeente.breda.bredaapp.fragment.MainScreenListFragment.EXTRA_REPORT;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailedReportActivity extends AppBaseActivity {
	
	private static final String TAG = "DetailedReportActivity";
	private TextView description, category;
	private ImageView mediaUrl;
	private Button extraReport;
	private boolean isPressed;
    private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailed_report);
		super.setMenuSelected(getIntent().getExtras());
		
		description = (TextView) findViewById(R.id.DetailedReportActivity_tv_kindOfDefectInput);
		mediaUrl = (ImageView) findViewById(R.id.DetailedReportActivity_iv_image);
		extraReport = (Button) findViewById(R.id.DetailedReportActivity_bt_extraReportBtn);
		category = (TextView) findViewById(R.id.DetailedReportActivity_tv_categoryInput);
		progressBar = (ProgressBar)findViewById(R.id.DetailedReportActivity_pb_imageProgressBar);
		
		Bundle extras = getIntent().getExtras();
		String getMediaUrl = extras.getString("MediaUrl");
		
		final Report r = (Report) extras.getSerializable(EXTRA_REPORT);
		
		category.setText(r.getServiceName());

		description.setText(r.getDescription());
		
		progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#d91d49"), PorterDuff.Mode.SRC_ATOP);
		
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
		
//		if(r.getMediaUrl() == null){
//			Picasso.with(getApplicationContext()).load(R.drawable.nopicturefound).into(mediaUrl);
//		} else {
//			Picasso.with(getApplicationContext()).load(getMediaUrl).into(mediaUrl, new Callback(){
//                @Override
//				public void onSuccess(){
//					progressBar.setVisibility(View.GONE);
//                    mediaUrl.setVisibility(View.VISIBLE);
//				}
//
//                @Override
//                public void onError() {
//                    Picasso.with(getApplicationContext()).load(R.drawable.nopicturefound).into(mediaUrl);
//                    progressBar.setVisibility(View.GONE);
//                }
//            });
//		}
//		new ImageLoader(mediaUrl).execute(r.getMediaUrl());
		
		mediaUrl.setOnClickListener(new View.OnClickListener() {
	
		@Override
		public void onClick(View v) {
			Log.i(TAG, "onClick geactiveerd.");
//			setContentView(R.layout.activity_detailed_report_fullscreen_image);
//			mediaUrl = (ImageView) findViewById(R.id.DetailedReportActivityFullscreenImage_IV_Image);
//			new ImageLoader(mediaUrl).execute(r.getMediaUrl());
//			Toast.makeText(getApplicationContext(), "FULLSCREEN!", Toast.LENGTH_LONG).show();
			Intent fullscreenImageIntent = new Intent(getApplicationContext(), DetailedReportActivityImage.class);
			fullscreenImageIntent.putExtra(EXTRA_REPORT, r);
			startActivity(fullscreenImageIntent);
		}
		
		});
		
		extraReport.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View v) {
				if(isPressed == false){
					extraReport.setBackgroundResource(R.drawable.onimage2);
					isPressed = true;
				} else if(isPressed == true){
					extraReport.setBackgroundResource(R.drawable.offimage);
					isPressed = false;
				}
			}
		});
	}
}
