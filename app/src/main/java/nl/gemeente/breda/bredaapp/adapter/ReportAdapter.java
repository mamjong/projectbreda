package nl.gemeente.breda.bredaapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.domain.Report;

public class ReportAdapter extends ArrayAdapter<Report> {
	
	private Report report;
	
	public ReportAdapter(Context context, ArrayList<Report> reports) {
		super(context, R.layout.fragment_list_view_row, reports);
	}
	
	@Override
	public View getView(int position, View convertViewInitial, ViewGroup parent) {
		
		// Create report
		report = getItem(position);
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
		
		timestamp.setText(convertTimeStamp(report.getRequestedDatetime()));
		
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
	
	// Format time/date from JSON object
	public String convertTimeStamp(String requestedDate) {

//		// Format: 2017-05-17T20:50:27+03:00 van Helsinki
		SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

		// Format example = 10-10-1010
		SimpleDateFormat reqDateFormat = new SimpleDateFormat("dd-MM-yyyy");

		Date date = null;
		try {
			date = sourceFormat.parse(requestedDate);
		} catch (ParseException e) {
			Log.e("ERR", e.getMessage());
		}

		return reqDateFormat.format(date);
	}
}