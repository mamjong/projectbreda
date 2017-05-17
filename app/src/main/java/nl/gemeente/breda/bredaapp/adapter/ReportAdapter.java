package nl.gemeente.breda.bredaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.domain.Report;

public class ReportAdapter extends ArrayAdapter<Report> {
	
	public ReportAdapter(Context context, ArrayList<Report> reports) {
		super(context, R.layout.fragment_list_view_row, reports);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// Create report
		Report report = getItem(position);
		
		// Check for existing view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_list_view_row, parent, false);
		}
			// Select row items
			TextView description = (TextView) convertView.findViewById(R.id.fragmentListViewRow_TV_description);
			TextView status = (TextView) convertView.findViewById(R.id.fragmentListViewRow_TV_status);
			
			// Get and set content
			description.setText(report.getDescription());
			status.setText(report.getStatus());
			
			// Return view
			return convertView;
		
	}
}