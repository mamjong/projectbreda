package nl.gemeente.breda.bredaapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Timer;
import java.util.TimerTask;

import nl.gemeente.breda.bredaapp.DetailedReportActivity;
import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.adapter.ReportAdapter;
import nl.gemeente.breda.bredaapp.businesslogic.ReportManager;
import nl.gemeente.breda.bredaapp.domain.Report;

public class MainScreenListFragment extends Fragment implements AdapterView.OnItemClickListener {
	
	public static final String EXTRA_REPORT = "REPORT";
	//================================================================================
	// Properties
	//================================================================================
	private static final String TAG = "MainScreenListFragment";
	private ReportAdapter reportAdapter;
	
	//================================================================================
	// Accessors
	//================================================================================
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_list_view, container, false);
		
		ListView reportsListView;
		reportsListView = (ListView) rootView.findViewById(R.id.homescreen_lv);
		
		reportAdapter = new ReportAdapter(getContext(), ReportManager.getReports()); //fragmentListView_LV_reportsList
		reportsListView.setAdapter(reportAdapter);
		reportsListView.setOnItemClickListener(this);
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				if (getActivity() == null)
					return;
				
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						reportAdapter.notifyDataSetChanged();
					}
				});
			}
		}, 0, 750);
		
		return rootView;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.i(TAG, "Report " + position + " is geselecteerd");
		
		Report r = ReportManager.getReports().get(position);
		Intent detailedReportIntent = new Intent(getContext(), DetailedReportActivity.class);
		detailedReportIntent.putExtra("MediaUrl", r.getMediaUrl());
		detailedReportIntent.putExtra("NoImage", R.drawable.nopicturefound);
		detailedReportIntent.putExtra(EXTRA_REPORT, r);
		startActivity(detailedReportIntent);
	}
}