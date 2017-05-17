package nl.gemeente.breda.bredaapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Timer;
import java.util.TimerTask;

import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.adapter.ReportAdapter;
import nl.gemeente.breda.bredaapp.businesslogic.ReportManager;

public class MainScreenListFragment extends Fragment {
	
	//================================================================================
	// Properties
	//================================================================================
	private ListView reportsListView;
	private ReportAdapter reportAdapter;

	//================================================================================
	// Accessors
	//================================================================================
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_list_view, container, false);
		
		reportsListView = (ListView) rootView.findViewById(R.id.homescreen_lv);
		
		reportAdapter = new ReportAdapter(getContext(), ReportManager.getReports()); //fragmentListView_LV_reportsList
		reportsListView.setAdapter(reportAdapter);
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				if(getActivity() == null)
					return;
				
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						reportAdapter.notifyDataSetChanged();
					}
				});
			}
		},0,750);
		
		return rootView;
	}
}