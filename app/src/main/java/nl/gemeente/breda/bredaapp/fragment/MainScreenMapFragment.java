package nl.gemeente.breda.bredaapp.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import nl.gemeente.breda.bredaapp.DetailedReportActivity;
import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.businesslogic.ReportManager;
import nl.gemeente.breda.bredaapp.domain.Report;

import static nl.gemeente.breda.bredaapp.fragment.MainScreenListFragment.EXTRA_REPORT;

public class MainScreenMapFragment extends Fragment implements OnMapReadyCallback {
	
	//================================================================================
	// Properties
	//================================================================================
	
	private GoogleMap map;
	private ArrayList<Report> reports;
	private boolean mapReady = false;
	private boolean startedLoading = false;
	private MapInfoWindowAdapter mapInfoWindowAdapter;
	
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
		mapReady = true;
		int themeID = R.style.AppTheme;
		try {
			themeID = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).applicationInfo.theme;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e("ERR", e.getMessage());
		}
		
		if (themeID == R.style.AppThemeNight) {
			MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_night);
			map.setMapStyle(style);
		}
		
		LatLngBounds breda = new LatLngBounds(new LatLng(51.482969, 4.654534), new LatLng(51.647188, 4.874748));
		mapInfoWindowAdapter = new MapInfoWindowAdapter();
		map.setLatLngBoundsForCameraTarget(breda);
		map.setMinZoomPreference(11);
		map.getUiSettings().setMapToolbarEnabled(false);
		map.getUiSettings().setRotateGesturesEnabled(false);
		map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(51.585811, 4.792396)));
		map.setInfoWindowAdapter(mapInfoWindowAdapter);
		map.setOnInfoWindowClickListener(mapInfoWindowAdapter);
		
		addMarkers();
	}
	
	public void addMarkers() {
		if (map == null) {
			return;
		}
		
		reports = ReportManager.getReports();
		
		for (Report report : reports) {
			double latitude = report.getLatitude();
			double longtitude = report.getLongitude();
			String description = report.getDescription();
			
			LatLng position = new LatLng(latitude, longtitude);
			
			MarkerOptions markerOptions = new MarkerOptions().position(position).title(description).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
			MainScreenMapFragment.this.map.addMarker(markerOptions);
		}
	}
	
	public void removeMarkers() {
		reports.clear();
		if (map != null) {
			map.clear();
		}
	}
	
	public GoogleMap getMap() {
		return map;
	}
	
	class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {
		
		private final View contentView;
		
		public MapInfoWindowAdapter() {
			contentView = LayoutInflater.from(getContext()).inflate(R.layout.infowindow_map, null);
		}
		
		
		@Override
		public View getInfoWindow(Marker marker) {
			return null;
		}
		
		@Override
		public View getInfoContents(Marker marker) {
			if (marker.getTitle() == null) {
				return null;
			}
			TextView tv_title = (TextView) contentView.findViewById(R.id.infoWindow_TV_Title);
			TextView tv_address = (TextView) contentView.findViewById(R.id.infoWindow_TV_Address);
			TextView tv_category = (TextView) contentView.findViewById(R.id.infoWindow_TV_Category);
			TextView tv_upvotes = (TextView) contentView.findViewById(R.id.infoWindow_TV_Upvotes);
			ImageView iv_image = (ImageView) contentView.findViewById(R.id.infoWindow_IV_Image);
			
			for (Report r : reports) {
				if (r.getDescription() == null) {
					return null;
				}
				
				Log.i("Marker Title", marker.getTitle());
				if (r.getDescription().equalsIgnoreCase(marker.getTitle())) {
					Log.i("Description", "Hit!");
					tv_title.setText(r.getDescription());
					tv_address.setText(r.getAddress());
					tv_category.setText(r.getServiceName());
					tv_upvotes.setText(r.getUpvotes() + "");
					Picasso.with(getContext()).load(r.getMediaUrl()).placeholder(R.drawable.nopicturefound).error(R.drawable.nopicturefound).into(iv_image);
					break;
				}
			}
			
			return contentView;
		}
		
		@Override
		public void onInfoWindowClick(Marker marker) {
			for (Report r : reports) {
				if (r.getDescription().equals(marker.getTitle())) {
					Intent intent = new Intent(getContext(), DetailedReportActivity.class);
					intent.putExtra("MediaUrl", r.getMediaUrl());
					intent.putExtra("NoImage", R.drawable.nopicturefound);
					intent.putExtra(EXTRA_REPORT, r);
					startActivity(intent);
					return;
				}
			}
		}
	}
}
