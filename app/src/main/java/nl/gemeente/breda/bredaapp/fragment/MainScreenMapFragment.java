package nl.gemeente.breda.bredaapp.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import nl.gemeente.breda.bredaapp.R;

public class MainScreenMapFragment extends Fragment implements OnMapReadyCallback {
	
	//================================================================================
	// Properties
	//================================================================================
	
	//================================================================================
	// Accessors
	//================================================================================
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_map_view, container, false);
		
		//SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.fragmentMapView_FR_map);
		//mapFragment.getMapAsync(this);
		
		return rootView;
	}
	
	@Override
	public void onMapReady(GoogleMap googleMap) {
		LatLng breda = new LatLng(51.5831, 4.777);
		float zoom = 12;
		googleMap.addMarker(new MarkerOptions().position(breda).title("Marker in Breda"));
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(breda, zoom));
	}
}
