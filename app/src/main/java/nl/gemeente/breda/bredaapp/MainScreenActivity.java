package nl.gemeente.breda.bredaapp;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import nl.gemeente.breda.bredaapp.adapter.MainScreenSectionsPagerAdapter;
import nl.gemeente.breda.bredaapp.adapter.ServiceAdapter;
import nl.gemeente.breda.bredaapp.api.ApiHomeScreen;
import nl.gemeente.breda.bredaapp.api.ApiServices;
import nl.gemeente.breda.bredaapp.businesslogic.ReportManager;
import nl.gemeente.breda.bredaapp.businesslogic.ServiceManager;
import nl.gemeente.breda.bredaapp.domain.Report;
import nl.gemeente.breda.bredaapp.domain.Service;
import nl.gemeente.breda.bredaapp.fragment.MainScreenMapFragment;

public class MainScreenActivity extends AppCompatActivity implements ApiHomeScreen.Listener, ApiServices.Listener, ApiHomeScreen.NumberOfReports, AdapterView.OnItemSelectedListener {
	
	//================================================================================
	// Properties
	//================================================================================
	
	private MainScreenSectionsPagerAdapter sectionsPagerAdapter;
	private ViewPager viewPager;
	private ServiceAdapter spinnerAdapter;
	private Spinner homescreenDropdown;
	private int numberOfReports;
	private TextView loading;
	private ImageView overlay;

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

		viewPager = (ViewPager) findViewById(R.id.container);
		viewPager.setAdapter(sectionsPagerAdapter);
		
		TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(viewPager);

		getReports("0");
		getServices();

		numberOfReports = -1;

		loading = (TextView) findViewById(R.id.activityMainscreen_tv_loading);
		overlay = (ImageView) findViewById(R.id.activityMainscreen_overlay_image);
		overlay.setVisibility(View.INVISIBLE);
		loading.setText(R.string.spinner_loading);
		
		homescreenDropdown = (Spinner) findViewById(R.id.homescreen_dropdown);
		homescreenDropdown.setVisibility(View.INVISIBLE);
		spinnerAdapter = new ServiceAdapter(getApplicationContext(), ServiceManager.getServices());
		homescreenDropdown.setAdapter(spinnerAdapter);
		homescreenDropdown.setOnItemSelectedListener(this);
		homescreenDropdown.setPrompt(getResources().getString(R.string.spinner_loading));
	}

	public void getReports(String serviceCode) {
		ApiHomeScreen apiHomeScreen = new ApiHomeScreen(this, this);
		String[] urls = new String[] {"https://asiointi.hel.fi/palautews/rest/v1/requests.json?status=open&service_code=" + serviceCode + "&lat=60.1892477&long=24.9707467&radius=5000"};
		apiHomeScreen.execute(urls);
	}

	public void getServices() {
		ApiServices apiServices = new ApiServices(this);
		String[] urls = new String[] {"https://asiointi.hel.fi/palautews/rest/v1/services.json"};
		apiServices.execute(urls);
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
		sectionsPagerAdapter.removeMarkers();
		Service service = ServiceManager.getServices().get(position);
		String serviceCode = service.getServiceCode();
		getReports(serviceCode);
		loading.setText(R.string.spinner_loading);
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
}