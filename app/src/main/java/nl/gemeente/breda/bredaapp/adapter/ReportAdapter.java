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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nl.gemeente.breda.bredaapp.DatabaseHandler;
import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.domain.Report;

//import nl.gemeente.breda.bredaapp.api.ImageLoader;


public class ReportAdapter extends ArrayAdapter<Report> {
	
	private Report report;
	
	public ReportAdapter(Context context, ArrayList<Report> reports) {
		super(context, R.layout.fragment_list_view_row, reports);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// Create report
		report = getItem(position);
		
		// Check for existing view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_list_view_row, parent, false);
		}
		
		// Select row items
		ImageView mediaUrl = (ImageView) convertView.findViewById(R.id.fragmentListViewRow_IV_mediaUrl);
		TextView description = (TextView) convertView.findViewById(R.id.fragmentListViewRow_TV_description);
		TextView status = (TextView) convertView.findViewById(R.id.fragmentListViewRow_TV_status);
		TextView category = (TextView) convertView.findViewById(R.id.fragmentListViewRow_TV_category);
		final ImageView imageCheckbox = (ImageView) convertView.findViewById(R.id.fragmentListViewRow_IV_imageCheckbox);
		
		// Tijdelijk om timestamp te testen
		TextView timestamp = (TextView) convertView.findViewById(R.id.fragmentListViewRow_TV_timeStamp);
		
		// Get and set content
		category.setText(report.getServiceName());
		
		timestamp.setText(convertTimeStamp(report.getRequestedDatetime()));
		
		description.setText(report.getDescription());
//		description.setText("subCategoryDescription");
		
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
		
		imageCheckbox.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
		
//		if (report.isFavorite()){
//			imageCheckbox.setImageResource(R.drawable.ic_check_box_black_24dp);
//		} else {
//			imageCheckbox.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
//		}
		
		// Handel het selecteren/deselecteren af
		// Deze PersonAdapter wordt in meerdere ListViews gebruikt: in de lijst met nieuw
		// toegevoegde personen, en in de lijst met Favorites.
//		imageCheckbox.setOnClickListener(new View.OnClickListener(){
//			@Override
//			public void onClick(View view) {
//				if (report.isFavorite() == true) {
//					report.setFavorite(false);
//					dbh.updateFavorite(report.isFavorite());
//
//				} else {
//					report.setFavorite(true);
//					dbh.updateFavorite(report.isFavorite());
//				}
//
//				if (report.isFavorite()){
//					imageCheckbox.setImageResource(R.drawable.ic_check_box_black_24dp);
//				} else {
//					imageCheckbox.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
//				}
//			}
//		});
		
		
		// ImageLoader sucks
//		new ImageLoader(mediaUrl).execute(report.getMediaUrl());
		
		// Return view
		return convertView;
	}
	
	// Format time/date from JSON object
	public String convertTimeStamp(String dateTime) {

//		// Format: 2017-05-17T20:50:27+03:00 van Helsinki
		SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

//		// Format example = 10-10-1010 om 10:10
//		SimpleDateFormat reqDateFormat = new SimpleDateFormat("dd-MM-YYYY 'om' HH:mm");
		
		// Format example = 10-10-1010
		SimpleDateFormat reqDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		Date date = null;
		try {
			date = sourceFormat.parse(report.getRequestedDatetime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String formatDateTime = reqDateFormat.format(date);
		return formatDateTime;
	}
}