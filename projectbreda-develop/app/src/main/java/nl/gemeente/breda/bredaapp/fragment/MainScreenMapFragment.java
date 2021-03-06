package nl.gemeente.breda.bredaapp.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.businesslogic.ReportManager;
import nl.gemeente.breda.bredaapp.businesslogic.ReportsMarkerHelper;
import nl.gemeente.breda.bredaapp.domain.Report;

public class MainScreenMapFragment extends Fragment implements OnMapReadyCallback {
	
	//================================================================================
	// Properties
	//================================================================================

	private GoogleMap map;

	//================================================================================
	// Accessors
	//================================================================================
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_map_view, container, false);

		SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragmentMapView_FL_mapLayout);
		if (mapFragment == null) {
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			mapFragment = SupportMapFragment.newInstance();
			fragmentTransaction.replace(R.id.fragmentMapView_FL_mapLayout, mapFragment).commit();
		}
		
		if (mapFragment != null) {
			mapFragment.getMapAsync(this);
		}
		
		return rootView;
	}
	
	@Override
	public void onMapReady(GoogleMap googleMap) {
//		LatLng breda = new LatLng(51.5853953, 4.7929303);
//		float zoom = 12;
//		googleMap.addMarker(new MarkerOptions().position(breda).title("Marker in Breda"));
//		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(breda, zoom));
		
//		ReportsMarkerHelper helper = new ReportsMarkerHelper(googleMap, getContext());
//		helper.demo();
		map = googleMap;

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ArrayList<Report> reports = ReportManager.getReports();

						for (Report report : reports) {
							double latitude = report.getLatitude();
							double longtitude = report.getLongitude();
							String description = report.getDescription();

							LatLng position = new LatLng(latitude, longtitude);

							MarkerOptions markerOptions = new MarkerOptions().position(position).title(description);
							MainScreenMapFragment.this.map.addMarker(markerOptions);
						}
					}
				});
			}
		},0,1000);
	}
}
