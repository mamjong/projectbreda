package nl.gemeente.breda.bredaapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import nl.gemeente.breda.bredaapp.DatabaseHandler;
import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.domain.Report;

import static nl.gemeente.breda.bredaapp.fragment.MainScreenListFragment.EXTRA_REPORT;

public class FavoriteReportsAdapter extends ArrayAdapter<Report> {
	
	private Report report;
	
	public FavoriteReportsAdapter(Context context, ArrayList<Report> reports) {
		super(context, R.layout.activity_favorite_reports_list_view_row, reports);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {

//		final DatabaseHandler dbh = new DatabaseHandler(getContext(), null, null, 1);
		
		// Create report
		report = getItem(position);
		
		// Check for existing view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_favorite_reports_list_view_row, parent, false);
		}
		
		// Select row items
		ImageView mediaUrl = (ImageView) convertView.findViewById(R.id.activityFavoriteReportsListViewRow_IV_mediaUrl);
		TextView description = (TextView) convertView.findViewById(R.id.activityFavoriteReportsListViewRow_TV_description);
		TextView status = (TextView) convertView.findViewById(R.id.activityFavoriteReportsListViewRow_TV_status);
		TextView category = (TextView) convertView.findViewById(R.id.activityFavoriteReportsListViewRow_TV_category);
		
		// Tijdelijk om timestamp te testen
		TextView timestamp = (TextView) convertView.findViewById(R.id.activityFavoriteReportsListViewRow_TV_timeStamp);
		
		// Get and set content
		category.setText(report.getServiceName());
		
//		timestamp.setText(convertTimeStamp(report.getRequestedDatetime()));
		
		description.setText(report.getDescription());
//		description.setText("subCategoryDescription");
		
		// First letter uppercase
		String reportStatus = report.getStatus();
		
		// Open = green, Closed = red
//		if (reportStatus.equals("open")) {
//			String colorGreen = "#58D68D";
//			status.setTextColor(Color.parseColor(colorGreen));
//		} else if (reportStatus.equals("closed")) {
//			String colorRed = "#E74C3C";
//			status.setTextColor(Color.parseColor(colorRed));
//		}
		
//		// First letter uppercase
//		String upperCaseStatus = reportStatus.substring(0, 1).toUpperCase() + reportStatus.substring(1);
//		status.setText(upperCaseStatus);
		
		// When image is not available
		Picasso.with(getContext()).load(report.getMediaUrl()).error(R.drawable.nopicturefound).into(mediaUrl);

		// Return view
		return convertView;
	}
}