package nl.gemeente.breda.bredaapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.floatingactionmenu.FloatingActionButton;
import com.flask.floatingactionmenu.FloatingActionMenu;
import com.flask.floatingactionmenu.FloatingActionToggleButton;
import com.flask.floatingactionmenu.OnFloatingActionMenuSelectedListener;

import nl.gemeente.breda.bredaapp.adapter.MainScreenSectionsPagerAdapter;
import nl.gemeente.breda.bredaapp.adapter.ServiceAdapter;
import nl.gemeente.breda.bredaapp.api.ApiHomeScreen;
import nl.gemeente.breda.bredaapp.api.LocationApi;
import nl.gemeente.breda.bredaapp.businesslogic.ReportManager;
import nl.gemeente.breda.bredaapp.businesslogic.ServiceManager;
import nl.gemeente.breda.bredaapp.domain.Report;
import nl.gemeente.breda.bredaapp.domain.Service;
import nl.gemeente.breda.bredaapp.util.AlertCreator;
import nl.gemeente.breda.bredaapp.util.ReverseGeocoder;

import static nl.gemeente.breda.bredaapp.UserSettingsActivity.PREFS_NAME;

public class MainScreenActivity extends AppBaseActivity implements ApiHomeScreen.Listener, ApiHomeScreen.NumberOfReports, AdapterView.OnItemSelectedListener, LocationApi.LocationListener {
	
	//================================================================================
	// Properties
	//================================================================================
	
	protected int reportRadius;
	private MainScreenSectionsPagerAdapter sectionsPagerAdapter;
	private TextView loading;
	private ImageView overlay;
	private double latitude;
	private double longtitude;
	private Context context;
	private String serviceCode;
	
	private int backPressAmount = 0;
	
	//================================================================================
	// Accessors
	//================================================================================
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		Bundle bundle = new Bundle();
		bundle.putInt("menuID", R.id.nav_reports);
		super.setMenuSelected(bundle);
		
		sectionsPagerAdapter = new MainScreenSectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext());
		
		latitude = 0;
		longtitude = 0;
		serviceCode = "0";
		
		ViewPager viewPager = (ViewPager) findViewById(R.id.container);
		viewPager.setAdapter(sectionsPagerAdapter);
		
		TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(viewPager);
		
		context = getApplicationContext();
		
		getReports("OV", 51.585811, 4.792396, 10000);
		getLocation();
		
		loading = (TextView) findViewById(R.id.activityMainscreen_tv_loading);
		overlay = (ImageView) findViewById(R.id.activityMainscreen_overlay_image);
		overlay.setVisibility(View.INVISIBLE);
		loading.setText(R.string.spinner_loading);
		
		Spinner homescreenDropdown = (Spinner) findViewById(R.id.homescreen_dropdown);
		
		ServiceAdapter spinnerAdapter = new ServiceAdapter(getApplicationContext(), ServiceManager.getServices(), R.layout.spinner_layout_adapter);
		homescreenDropdown.setAdapter(spinnerAdapter);
		homescreenDropdown.setOnItemSelectedListener(this);
		homescreenDropdown.setPrompt(getResources().getString(R.string.spinner_loading));
		
		SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		reportRadius = preferences.getInt("ReportRadius", 500);
		
		spinnerAdapter.notifyDataSetChanged();
		
		FloatingActionMenu floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fam);
		floatingActionMenu.setOnFloatingActionMenuSelectedListener(new OnFloatingActionMenuSelectedListener() {
			@Override
			public void onFloatingActionMenuSelected(FloatingActionButton floatingActionButton) {
				if (floatingActionButton instanceof FloatingActionToggleButton) {
					// not used
				} else if (floatingActionButton instanceof FloatingActionButton) {
					MainScreenActivity.super.onMenuClick(CreateNewReportActivity.class, -1, false);
				}
			}
		});
	}
	
	public void getReports(String serviceCode, double latitude, double longtitude, int radius) {
		ReportManager.emptyArray();
		ApiHomeScreen apiHomeScreen = new ApiHomeScreen(this, this);
		String[] urls = new String[]{
				"http://37.34.59.50/breda/CitySDK/requests.json"
		};
//		String[] urls = new String[]{"http://37.34.59.50/breda/CitySDK/requests.json?status=open&service_code=" + serviceCode + "&lat=" + latitude + "&long=" + longtitude + "&radius=" + radius};
		apiHomeScreen.execute(urls);
	}
	
	public void getLocation() {
		LocationApi locationApi = new LocationApi(this);
		locationApi.setContext(context, this);
		locationApi.search();
	}
	
	@Override
	public void onReportAvailable(Report report) {
		if (report.getLongitude() > 1 && report.getLatitude() > 1){
			ReverseGeocoder reverseGeocoder = new ReverseGeocoder(report.getLatitude(), report.getLongitude(), context);
			report.setAddress(reverseGeocoder.getAddress());
		}
		
		ReportManager.addReport(report);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		ReportManager.emptyArray();
		sectionsPagerAdapter.removeMarkers();
		Service service = ServiceManager.getServices().get(position);
		serviceCode = service.getServiceCode();
		
		Float zero = 0.f;
		Float lat = new Float(latitude);
		Float lon = new Float(longtitude);
		
		if (lat.equals(zero) || lon.equals(zero)) {
			getReports(serviceCode, 60.1892477, 24.9707467, 10000);
		} else {
			getReports(serviceCode, latitude, longtitude, reportRadius);
		}
		
		loading.setText(R.string.spinner_loading);
		overlay.setVisibility(View.VISIBLE);
		if (sectionsPagerAdapter.getMap() != null) {
			sectionsPagerAdapter.getMap().getUiSettings().setScrollGesturesEnabled(false);
		}
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		return;
	}
	
	@Override
	public void onNumberOfReportsAvailable(int number) {
		if (number > 0) {
			if (sectionsPagerAdapter.getMap() != null) {
				sectionsPagerAdapter.getMap().getUiSettings().setScrollGesturesEnabled(true);
			}
			loading.setText("");
			overlay.setVisibility(View.INVISIBLE);
		} else if (number == 0) {
			if (sectionsPagerAdapter.getMap() != null) {
				sectionsPagerAdapter.getMap().getUiSettings().setScrollGesturesEnabled(false);
			}
			loading.setText(R.string.no_reports_found);
			overlay.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void noConnectionAvailable() {
		Toast toast = Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == 1) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				finish();
				startActivity(getIntent());
			} else {
				AlertCreator alertCreator = new AlertCreator(MainScreenActivity.this);
				
				alertCreator.setTitle(R.string.no_location_permission_title);
				alertCreator.setMessage(R.string.no_location_permission_description);
				alertCreator.setNegativeButton(R.string.no_location_permission_negative, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
				alertCreator.setPositiveButton(R.string.no_location_permission_positive, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ActivityCompat.requestPermissions(MainScreenActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
					}
				});
				alertCreator.show();
			}
			return;
		}
	}
	
	@Override
	public void onLocationAvailable(double latitude, double longtitude) {
		Log.i("LOCATION", latitude + ":" + longtitude);
		this.latitude = latitude;
		this.longtitude = longtitude;
		getReports(serviceCode, latitude, longtitude, reportRadius);
		
		ReverseGeocoder geocoder = new ReverseGeocoder(latitude, longtitude, this);
	}
	
	@Override
	public void onBackPressed() {
		if (backPressAmount == 0) {
			Toast toast = Toast.makeText(MainScreenActivity.this, "Press again to exit.", Toast.LENGTH_LONG);
			toast.show();
			backPressAmount = 1;
			
			new CountDownTimer(2000, 1000) {
				@Override
				public void onTick(long millisUntilFinished) {
					// moeten we nog doen
				}
				
				@Override
				public void onFinish() {
					backPressAmount = 0;
				}
			}.start();
		} else if (backPressAmount == 1) {
			System.exit(0);
		}
	}
}