package nl.gemeente.breda.bredaapp;

import android.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import nl.gemeente.breda.bredaapp.adapter.MainScreenSectionsPagerAdapter;
import nl.gemeente.breda.bredaapp.adapter.ServiceAdapter;
import nl.gemeente.breda.bredaapp.api.ApiHomeScreen;
import nl.gemeente.breda.bredaapp.api.ApiServices;
import nl.gemeente.breda.bredaapp.api.LocationApi;
import nl.gemeente.breda.bredaapp.businesslogic.ReportManager;
import nl.gemeente.breda.bredaapp.businesslogic.ServiceManager;
import nl.gemeente.breda.bredaapp.domain.Report;
import nl.gemeente.breda.bredaapp.domain.Service;
import nl.gemeente.breda.bredaapp.util.AlertCreator;

public class MainScreenActivity extends AppCompatActivity implements ApiHomeScreen.Listener, ApiServices.Listener, ApiHomeScreen.NumberOfReports, AdapterView.OnItemSelectedListener, LocationApi.LocationListener {
	
	//================================================================================
	// Properties
	//================================================================================
	
	private MainScreenSectionsPagerAdapter sectionsPagerAdapter;
	private ViewPager viewPager;
	private ReportManager reportManager;
	private Button newReportActivityBtn;
	private ServiceAdapter spinnerAdapter;
	private Spinner homescreenDropdown;
	private int numberOfReports;
	private TextView loading;
	private ImageView overlay;
	private double latitude;
	private double longtitude;
	private Context context;
	private String serviceCode;

	//================================================================================
	// Accessors
	//================================================================================
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		sectionsPagerAdapter = new MainScreenSectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext());

		latitude = 0;
		longtitude = 0;
		serviceCode = "0";

		viewPager = (ViewPager) findViewById(R.id.container);
		viewPager.setAdapter(sectionsPagerAdapter);
		
		TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(viewPager);

		newReportActivityBtn = (Button) findViewById(R.id.mainScreenActivity_Btn_MakeReport);

		newReportActivityBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), CreateNewReportActivity.class);
				startActivity(i);
			}
		});
		
		context = getApplicationContext();
		
		getReports("0", 60.1892477, 24.9707467, 10000);
		getServices();
		getLocation();

		numberOfReports = -1;

		loading = (TextView) findViewById(R.id.activityMainscreen_tv_loading);
		overlay = (ImageView) findViewById(R.id.activityMainscreen_overlay_image);
		overlay.setVisibility(View.INVISIBLE);
		loading.setText(R.string.spinner_loading);
		
		homescreenDropdown = (Spinner) findViewById(R.id.homescreen_dropdown);

		homescreenDropdown.setVisibility(View.INVISIBLE);
		spinnerAdapter = new ServiceAdapter(getApplicationContext(), ServiceManager.getServices(), R.layout.spinner_layout_adapter);
		homescreenDropdown.setAdapter(spinnerAdapter);
		homescreenDropdown.setOnItemSelectedListener(this);
		homescreenDropdown.setPrompt(getResources().getString(R.string.spinner_loading));
	}

	public void getReports(String serviceCode, double latitude, double longtitude, int radius) {
		ApiHomeScreen apiHomeScreen = new ApiHomeScreen(this, this);
		String[] urls = new String[] {"https://asiointi.hel.fi/palautews/rest/v1/requests.json?status=open&service_code=" + serviceCode + "&lat=" + latitude + "&long=" + longtitude + "&radius=" + radius};
		apiHomeScreen.execute(urls);
	}

	public void getServices() {
		ApiServices apiServices = new ApiServices(this);
		String[] urls = new String[] {"https://asiointi.hel.fi/palautews/rest/v1/services.json"};
		apiServices.execute(urls);
	}
	
	public void getLocation(){
		LocationApi locationApi = new LocationApi(this);
		locationApi.setContext(context, this);
		locationApi.search();
	}

	@Override
	public void onReportAvailable(Report report) {
		//Log.i("Report", report.getDescription());
		ReportManager.addReport(report);
	}

	@Override
	public void onServiceAvailable(Service service) {
		//Log.i("Service", service.getServiceName());
		homescreenDropdown.setVisibility(View.VISIBLE);
		ServiceManager.addService(service);
		spinnerAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		ReportManager.emptyArray();
		sectionsPagerAdapter.removeMarkers();
		Service service = ServiceManager.getServices().get(position);
		serviceCode = service.getServiceCode();
		
		if(latitude == 0 || longtitude == 0) {
			getReports(serviceCode, 60.1892477, 24.9707467, 10000);
		}
		else {
			getReports(serviceCode, latitude, longtitude, 500);
		}
		
		loading.setText(R.string.spinner_loading);
		overlay.setVisibility(View.VISIBLE);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public void onNumberOfReportsAvailable(int number) {
		this.numberOfReports = number;
		if(number > 0){
			loading.setText("");
			overlay.setVisibility(View.INVISIBLE);
		}
		else if(number == 0){
			loading.setText(R.string.no_reports_found);
			overlay.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void noConnectionAvailable() {
		Toast toast = Toast.makeText(this, "No connection available.", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case 1: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					finish();
					startActivity(getIntent());
				} else {
					AlertCreator alertCreator = new AlertCreator(context);
					
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
	}
	
	@Override
	public void onLocationAvailable(double latitude, double longtitude) {
		//Log.i("LOCATION", latitude + ":" + longtitude);
		this.latitude = latitude;
		this.longtitude = longtitude;
		getReports(serviceCode, latitude, longtitude, 500);
	}
}