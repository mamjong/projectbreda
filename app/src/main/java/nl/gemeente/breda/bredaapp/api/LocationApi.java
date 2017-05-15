package nl.gemeente.breda.bredaapp.api;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import nl.gemeente.breda.bredaapp.MainScreenActivity;
import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.domain.Service;
import nl.gemeente.breda.bredaapp.testing.LocationActivity;
import nl.gemeente.breda.bredaapp.util.AlertCreator;

public class LocationApi implements ConnectionCallbacks, OnConnectionFailedListener {
	
	protected GoogleApiClient mGoogleApiClient;
	public Location mLastLocation;
	private Context context;
	private Activity activity;
	private double latitude;
	private double longtitude;
	private LocationListener listener = null;
	
	public LocationApi(LocationListener listener) {
		this.listener = listener;
	}
	
	public void setContext(Context context, Activity activity) {
		this.context = context;
		this.activity = activity;
	}
	
	public void search(){
		buildGoogleApiClient();
		mGoogleApiClient.connect();
	}
	
	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(context)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
			}
		}
		mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if (mLastLocation != null) {
			latitude = mLastLocation.getLatitude();
			longtitude = mLastLocation.getLongitude();
			listener.onLocationAvailable(latitude, longtitude);
		} else {
			// No location found
			// plantVirus();
		}
	}
	
	@Override
	public void onConnectionSuspended(int cause) {
		Log.i("CONNECTION SUSPENDED", "Connection suspended");
		mGoogleApiClient.connect();
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.i("CONNECTION FAILED", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
	}
	
	public interface LocationListener {
		void onLocationAvailable(double latitude, double longtitude);
	}
}
