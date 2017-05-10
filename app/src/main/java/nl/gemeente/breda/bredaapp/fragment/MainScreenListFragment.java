package nl.gemeente.breda.bredaapp.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.adapter.ReportAdapter;
import nl.gemeente.breda.bredaapp.businesslogic.ReportManager;
import nl.gemeente.breda.bredaapp.domain.Report;

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

		reportAdapter = new ReportAdapter(getActivity().getParent().getApplicationContext(), ReportManager.getReports()); //fragmentListView_LV_reportsList
		reportsListView.setAdapter(reportAdapter);
		
		
		return rootView;
	}
	

}