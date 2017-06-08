package nl.gemeente.breda.bredaapp;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import nl.gemeente.breda.bredaapp.adapter.FavoriteReportsAdapter;
import nl.gemeente.breda.bredaapp.adapter.ReportAdapter;
import nl.gemeente.breda.bredaapp.api.ApiHomeScreen;
import nl.gemeente.breda.bredaapp.businesslogic.FavoriteReportManager;
import nl.gemeente.breda.bredaapp.businesslogic.ReportManager;
import nl.gemeente.breda.bredaapp.domain.Report;

import static nl.gemeente.breda.bredaapp.fragment.MainScreenListFragment.EXTRA_REPORT;

public class FavoriteReportsActivity extends AppBaseActivity implements ApiHomeScreen.Listener, ApiHomeScreen.NumberOfReports {
	
	public static final String TAG = "FavoriteReportsActivity";
	private ListView favoriteReportsListView;
	private FavoriteReportsAdapter favoriteReportsAdapter;
	private ArrayList<Report> reports = new ArrayList<Report>();
	private String serviceRequestId;
	private int numberOfReports;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_reports);
		super.setMenuSelected(getIntent().getExtras());
		
		final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext(), null, null, 1);
		numberOfReports = -1;
		favoriteReportsListView = (ListView) findViewById(R.id.favoritescreen_lv);
		
		//if (reports.contains(report.getServiceRequestId())) {
		reports = (ArrayList<Report>) dbh.getAllReports();
		
		Log.i(TAG, "We hebben " + reports.size() + " favorites");
		
	
		for (Report report : reports) {
			serviceRequestId = report.getServiceRequestId();
			Log.i(TAG, "ID = " + report.getServiceRequestId());
			
	//		favoriteReportsAdapter = new FavoriteReportsAdapter(getApplicationContext(), FavoriteReportManager.getFavoriteReports());
			favoriteReportsAdapter = new FavoriteReportsAdapter(getApplicationContext(), reports);
			favoriteReportsListView.setAdapter(favoriteReportsAdapter);
	
			getFavoriteReports(serviceRequestId);
		}
	}
	
	public void getFavoriteReports(String serviceRequestId) {
		FavoriteReportManager.emptyArray();
		ApiHomeScreen apiHomeScreen = new ApiHomeScreen(this, this);
		String[] urls = new String[]{"https://asiointi.hel.fi/palautews/rest/v1/requests/" + serviceRequestId + ".json"};
		apiHomeScreen.execute(urls);
	}
	
	
	
	@Override
	public void onReportAvailable(Report report) {
		Log.i("Report", report.getDescription());
		ReportManager.addReport(report);
		favoriteReportsAdapter.notifyDataSetChanged();
	}

	@Override
	public void noConnectionAvailable() {
		Toast toast = Toast.makeText(this, "No connection available.", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	@Override
	public void onNumberOfReportsAvailable(int number) {
		this.numberOfReports = number;
	}

}
