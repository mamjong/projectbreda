//================================================================================
// This class is made by:
// - Thimo Koolen
//================================================================================

package nl.gemeente.breda.bredaapp.businesslogic;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Random;

import nl.gemeente.breda.bredaapp.domain.ReportMarker;

public class ReportsMarkerHelper {
	
	//================================================================================
	// Properties
	//================================================================================
	
	private ArrayList<ReportMarker> reportMarkers;
	private GoogleMap map;
	private Context context;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	public ReportsMarkerHelper(GoogleMap map, Context context) {
		reportMarkers = new ArrayList<>();
		this.map = map;
		this.context = context;
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	public ArrayList<ReportMarker> getReportMarkers() {
		return reportMarkers;
	}
	
	//================================================================================
	// Mutators
	//================================================================================
	
	/**
	 * Load reported data from the API and add a marker
	 */
	public void loadData() {
		demo(); //For now, load demo data
	}
	
	/**
	 * Temporary class for demo generated reported data
	 */
	public void demo() {
		//Between lat: 51.585650 - 51.587747
		//Between long: 4.749715 - 4.789626
		MarkerOptions lastMarker = null;
		for (int i = 0; i < 20; i++) {
			Random r = new Random();
			double latitude = 51.585650 + (51.587747 - 51.585650) * r.nextDouble();
			double longitude = 4.749715 + (4.789626 - 4.749715) * r.nextDouble();
			LatLng position = new LatLng(latitude, longitude);
			MarkerOptions marker = new MarkerOptions().position(position).title("Marker #" + i).snippet("Latitude: " + latitude + "\n" + "Longitude: " + longitude);
			map.addMarker(marker);
			map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
				@Override
				public View getInfoWindow(Marker marker) {
					return null;
				}
				
				@Override
				public View getInfoContents(Marker marker) {
					LinearLayout info = new LinearLayout(context);
					info.setOrientation(LinearLayout.VERTICAL);
					
					TextView title = new TextView(context);
					title.setTextColor(Color.BLACK);
					title.setGravity(Gravity.CENTER_HORIZONTAL);
					title.setTypeface(null, Typeface.BOLD);
					title.setText(marker.getTitle());
					
					TextView snippet = new TextView(context);
					snippet.setTextColor(Color.GRAY);
					snippet.setText(marker.getSnippet());
					
					info.addView(title);
					info.addView(snippet);
					
					return info;
				}
			});
			lastMarker = marker;
		}
		
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastMarker.getPosition(), 12));
	}
	
	/**
	 * Add a new marker to the current list
	 * @param latitude location latitude value
	 * @param longitude location longitude value
	 * @param title marker title value
	 */
	public void addMarker(double latitude, double longitude, String title) {
		ReportMarker marker = new ReportMarker(latitude, longitude, title);
		reportMarkers.add(marker);
	}
}
