package nl.gemeente.breda.bredaapp.util;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class LocationTracker extends Service implements LocationListener {
	
	//================================================================================
	// Properties
	//================================================================================
	
	private final Context context;
	private boolean isGPSEnabled = false;
	private boolean isNetworkEnabled = false;
	private boolean canGetLocation = false;
	private Location location;
	private double latitude;
	private double longitude;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
	protected LocationManager locationManager;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	public LocationTracker(Context context) {
		this.context = context;
		getLocation();
	}
	
	public Location getLocation() {
		try {
			locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
			
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			if (!isGPSEnabled && !isNetworkEnabled) {
				showNoLocationAlert();
			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
						return null;
					}
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return location;
	}
	
	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(LocationTracker.this);
		}
	}
	
	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}
		
		return latitude;
	}
	
	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}
		
		return longitude;
	}
	
	public boolean canGetLocation() {
		return this.canGetLocation;
	}
	
	public void showNoLocationAlert() {
		AlertCreator creator = new AlertCreator(context);
		creator.setTitle("GPS settings");
		creator.setMessage("GPS is not enabled. Do you want to enable the GPS?");
		
		ButtonDefine positve = new ButtonDefine("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				context.startActivity(intent);
			}
		});
		
		ButtonDefine negative = new ButtonDefine("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.cancel();
			}
		});
		
		creator.setPositiveButton(positve);
		creator.setNegativeButton(negative);
		
		creator.show();
	}
	
	@Override
	public void onLocationChanged(Location location) {
		
	}
	
	@Override
	public void onProviderDisabled(String provider) {
		
	}
	
	@Override
	public void onProviderEnabled(String provider) {
		
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
