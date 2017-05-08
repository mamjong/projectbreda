//================================================================================
// This class is made by:
// - Thimo Koolen
//================================================================================

package nl.gemeente.breda.bredaapp.businesslogic;

import android.content.Context;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
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
	
	//================================================================================
	// Constructors
	//================================================================================
	
	public ReportsMarkerHelper(GoogleMap map) {
		reportMarkers = new ArrayList<>();
		this.map = map;
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
		//Between long: 4.758759 - 4.781890
		//Between long: 4.749715 - 4.789626
		MarkerOptions lastMarker = null;
		for (int i = 0; i < 20; i++) {
			Random r = new Random();
			double latitude = 51.585650 + (51.587747 - 51.585650) * r.nextDouble();
			double longitude = 4.749715 + (4.789626 - 4.749715) * r.nextDouble();
			LatLng position = new LatLng(latitude, longitude);
			MarkerOptions marker = new MarkerOptions().position(position).title("Marker #" + i);
			map.addMarker(marker);
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
