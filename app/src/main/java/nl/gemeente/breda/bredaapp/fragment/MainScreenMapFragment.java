package nl.gemeente.breda.bredaapp.fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.businesslogic.ReportManager;
import nl.gemeente.breda.bredaapp.domain.Report;

public class MainScreenMapFragment extends Fragment implements OnMapReadyCallback {
	
	//================================================================================
	// Properties
	//================================================================================
	
	private GoogleMap map;
	private ArrayList<Report> reports;
	
	//================================================================================
	// Accessors
	//================================================================================
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_map_view, container, false);
		
		reports = new ArrayList<>();
		
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
		map = googleMap;
		int themeID = R.style.AppTheme;
		try {
			themeID = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).applicationInfo.theme;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		
		if (themeID == R.style.AppThemeNight) {
			MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_night);
			map.setMapStyle(style);
		}
		
		LatLngBounds helsinki = new LatLngBounds(new LatLng(60.08, 24.76), new LatLng(60.26, 25.08));
		map.setLatLngBoundsForCameraTarget(helsinki);
		map.setMinZoomPreference(11);
		map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(60.192059, 24.945831)));
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				if(getActivity() == null)
					return;
				
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						reports = ReportManager.getReports();
						
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
		}, 0, 750);
	}
	
	public void removeMarkers() {
		reports.clear();
		if(map != null) {
			map.clear();
		}
	}
	
	public GoogleMap getMap() {
		return map;
	}
}
