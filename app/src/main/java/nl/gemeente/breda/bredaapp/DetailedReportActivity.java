package nl.gemeente.breda.bredaapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;

import nl.gemeente.breda.bredaapp.domain.Report;
import nl.gemeente.breda.bredaapp.fragment.MainScreenMapFragment;
import nl.gemeente.breda.bredaapp.util.ReverseGeocoder;

import static nl.gemeente.breda.bredaapp.fragment.MainScreenListFragment.EXTRA_REPORT;

public class DetailedReportActivity extends AppBaseActivity implements OnMapReadyCallback {
	
	private static final String TAG = "DetailedReportActivity";
	private Button extraReport;
	private boolean isPressed;
	private ProgressBar progressBar;
	private int upvotes;
	
	private Report r;
	
	private GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailed_report);
		super.setMenuSelected(getIntent().getExtras());
		
		TextView description = (TextView) findViewById(R.id.DetailedReportActivity_tv_commentText);
		ImageView mediaUrl = (ImageView) findViewById(R.id.DetailedReportActivity_iv_image);
		extraReport = (Button) findViewById(R.id.DetailedReportActivity_bt_extraReportBtn);
		TextView category = (TextView) findViewById(R.id.DetailedReportActivity_tv_categoryInput);
		progressBar = (ProgressBar) findViewById(R.id.DetailedReportActivity_pb_imageProgressBar);
		final TextView count = (TextView) findViewById(R.id.DetailedReportActivity_tv_reportCounter);
		ImageView directions = (ImageView) findViewById(R.id.DetailedReportActivity_iv_directions); 
		
		Bundle extras = getIntent().getExtras();
		String getMediaUrl = extras.getString("MediaUrl");
		int getNoImage = extras.getInt("NoImage");
		
		r = (Report) extras.getSerializable(EXTRA_REPORT);
		final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext(), null, null, 1);
		
		double lat = r.getLatitude();
		double lng = r.getLongitude();
		
		directions.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri intentUri = Uri.parse("geo:" + r.getLatitude() + "," + r.getLongitude() + "?z=18");
				Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
				intent.setPackage("com.google.android.apps.maps");
				startActivity(intent);
			}
		});
		
		//ReverseGeocoder geocoder = new ReverseGeocoder(lat, lng, this);
		TextView address = (TextView) findViewById(R.id.DetailedReportActivity_tv_address);
		if ((r.getAddress() == null) || (r.getAddress().isEmpty()) || (r.getAddress().equals(""))) {
			String address_content = r.getAddress();
			if (address_content == null) {
				address_content = address_content.replaceAll("(?<=(^|\\G)\\S{0,100}\\s\\S{0,100})\\s", "\n");
				address.setText(address_content);
			}
		}
		
		
		category.setText(r.getServiceName());
		upvotes = r.getUpvotes();
		count.setText(Integer.toString(upvotes));
		
		description.setText(r.getDescription());
		
		progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#d91d49"), PorterDuff.Mode.SRC_ATOP);
		

		if (dbh.checkReport(r) == true) {
			extraReport.setBackgroundResource(R.drawable.ic_check_black_48dp);
			isPressed = true;
		} else if (dbh.checkReport(r) == false) {
			extraReport.setBackgroundResource(R.drawable.ic_plus_one_black_48dp);
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
					extraReport.setBackgroundResource(R.drawable.ic_check_black_48dp);
					Log.i(TAG, "melding = checked");
					upvotes++;
					count.setText(Integer.toString(upvotes));
					isPressed = true;
					Toast toast = Toast.makeText(DetailedReportActivity.this, getResources().getString(R.string.toast_report_added), Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					
				} else if (isPressed == true) {
					dbh.deleteReport(r);
					extraReport.setBackgroundResource(R.drawable.ic_plus_one_black_48dp);
					Log.i(TAG, "melding = unchecked");
					upvotes--;
					count.setText(Integer.toString(upvotes));
					isPressed = false;
					Toast toast = Toast.makeText(DetailedReportActivity.this, getResources().getString(R.string.toast_report_deleted), Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		});
		
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMapView_FL_mapLayout);
		if (mapFragment == null) {
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			mapFragment = SupportMapFragment.newInstance();
			transaction.replace(R.id.fragmentMapView_FL_mapLayout, mapFragment).commit();
		}
		
		if (mapFragment != null) {
			mapFragment.getMapAsync(this);
		}
	}
	
	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		
		LatLng latLng = new LatLng(r.getLatitude(), r.getLongitude());
		
		MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
		map.addMarker(markerOptions);
		LatLngBounds breda = new LatLngBounds(new LatLng(51.482969, 4.654534), new LatLng(51.647188, 4.874748));
		map.setLatLngBoundsForCameraTarget(breda);
		map.setMinZoomPreference(11);
		map.getUiSettings().setMapToolbarEnabled(false);
		map.getUiSettings().setRotateGesturesEnabled(false);
		map.getUiSettings().setAllGesturesEnabled(false);
		map.getUiSettings().setCompassEnabled(false);
		map.getUiSettings().setRotateGesturesEnabled(false);
		map.getUiSettings().setMyLocationButtonEnabled(false);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.f));
	}
}
