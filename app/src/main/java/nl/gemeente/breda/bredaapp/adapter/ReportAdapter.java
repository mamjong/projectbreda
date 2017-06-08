package nl.gemeente.breda.bredaapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.domain.Report;
import nl.gemeente.breda.bredaapp.util.TimeStampFormat;


public class ReportAdapter extends ArrayAdapter<Report> {
	
	private Report report;
	
	public ReportAdapter(Context context, ArrayList<Report> reports) {
		super(context, R.layout.fragment_list_view_row, reports);
	}
	
	@Override
	public View getView(int position, View convertViewInitial, ViewGroup parent) {
		
		// Create report
		report = getItem(position);
		TimeStampFormat tsf = new TimeStampFormat();
		View convertView = convertViewInitial;
		
		// Check for existing view
		if (convertViewInitial == null) {
			convertViewInitial = LayoutInflater.from(getContext()).inflate(R.layout.fragment_list_view_row, parent, false);
		}
		
		// Select row items
		ImageView mediaUrl = (ImageView) convertViewInitial.findViewById(R.id.fragmentListViewRow_IV_mediaUrl);
		TextView description = (TextView) convertViewInitial.findViewById(R.id.fragmentListViewRow_TV_description);
		TextView status = (TextView) convertViewInitial.findViewById(R.id.fragmentListViewRow_TV_status);
		TextView category = (TextView) convertViewInitial.findViewById(R.id.fragmentListViewRow_TV_category);
		
		// Tijdelijk om timestamp te testen
		TextView timestamp = (TextView) convertViewInitial.findViewById(R.id.fragmentListViewRow_TV_timeStamp);
		
		// Get and set content
		category.setText(report.getServiceName());
		
		String getRequestedDate = report.getRequestedDatetime();
		tsf.setTime(getRequestedDate);
		String requestedDate = tsf.getTime();
		String formattedDate = tsf.convertTimeStamp(requestedDate);
		timestamp.setText(formattedDate);
		
		description.setText(report.getDescription());
		
		// First letter uppercase
		String reportStatus = report.getStatus();
		
		// Open = green, Closed = red
		if (reportStatus.equals("open")) {
			String colorGreen = "#58D68D";
			status.setTextColor(Color.parseColor(colorGreen));
		} else if (reportStatus.equals("closed")) {
			String colorRed = "#E74C3C";
			status.setTextColor(Color.parseColor(colorRed));
		}
		
		// First letter uppercase
		String upperCaseStatus = reportStatus.substring(0, 1).toUpperCase() + reportStatus.substring(1);
		status.setText(upperCaseStatus);
		
		// When image is not available
		if (report.getMediaUrl() == null) {
			Picasso.with(getContext()).load(R.drawable.nopicturefound).into(mediaUrl);
		} else {
			Picasso.with(getContext()).load(report.getMediaUrl()).into(mediaUrl);
		}
		
		// Return view
		return convertViewInitial;
	}
}