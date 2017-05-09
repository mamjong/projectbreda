package nl.gemeente.breda.bredaapp;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import nl.gemeente.breda.bredaapp.adapter.MainScreenSectionsPagerAdapter;
import nl.gemeente.breda.bredaapp.api.ApiHomeScreen;
import nl.gemeente.breda.bredaapp.api.ApiServices;
import nl.gemeente.breda.bredaapp.businesslogic.ReportManager;
import nl.gemeente.breda.bredaapp.businesslogic.ServiceManager;
import nl.gemeente.breda.bredaapp.domain.Report;
import nl.gemeente.breda.bredaapp.domain.Service;
import nl.gemeente.breda.bredaapp.fragment.MainScreenMapFragment;

public class MainScreenActivity extends AppCompatActivity implements ApiHomeScreen.Listener, ApiServices.Listener {
	
	//================================================================================
	// Properties
	//================================================================================
	
	private MainScreenSectionsPagerAdapter sectionsPagerAdapter;
	private ViewPager viewPager;
	private ArrayList<String> servicesNames;

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

		getReports();
		getServices();

		servicesNames = new ArrayList<>();
		Spinner homescreenDropdown = (Spinner) findViewById(R.id.homescreen_dropdown);
		final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout_adapter, servicesNames);
		homescreenDropdown.setAdapter(spinnerAdapter);

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						servicesNames.clear();

						ArrayList<Service> services = ServiceManager.getServices();

						for (Service service : services) {
							servicesNames.add(service.getServiceName());
							spinnerAdapter.notifyDataSetChanged();
						}
					}
				});
			}
		},0,1500);
	}

	public void getReports() {
		ApiHomeScreen apiHomeScreen = new ApiHomeScreen(this);
		String[] urls = new String[] {"https://asiointi.hel.fi/palautews/rest/v1/requests.json?status=open&service_code=2806&lat=60.1892477&long=24.9707467&radius=5000"};
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
		Log.i("Service", service.getServiceName());
		ServiceManager.addService(service);
	}
}