package nl.gemeente.breda.bredaapp.testing;

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

import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.util.AlertCreator;

public class LocationActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {
	
	protected static final String TAG = "LocationActivity";
	
	protected GoogleApiClient mGoogleApiClient;
	public Location mLastLocation;
	
//	protected String mLatitudeLabel;
//	protected String mLongitudeLabel;
//	protected TextView mLatitudeText;
//	protected TextView mLongitudeText;
	
	private Context context = getApplicationContext();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		
//		mLatitudeLabel = getResources().getString(R.string.latitude_label);
//		mLongitudeLabel = getResources().getString(R.string.longitude_label);
//		mLatitudeText = (TextView) findViewById((R.id.latitude_text));
//		mLongitudeText = (TextView) findViewById((R.id.longitude_text));
		
		buildGoogleApiClient();
	}
	
	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
			}
		}
		mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if (mLastLocation != null) {
//			mLatitudeText.setText(String.format("%s: %f", mLatitudeLabel, mLastLocation.getLatitude()));
//			mLongitudeText.setText(String.format("%s: %f", mLongitudeLabel, mLastLocation.getLongitude()));
			
			
		} else {
			// No location found
			// plantVirus();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case 1: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					finish();
					startActivity(getIntent());
				} else {
					AlertCreator alertCreator = new AlertCreator(context);
					
					alertCreator.setTitle(R.string.no_location_permission_title);
					alertCreator.setMessage(R.string.no_location_permission_description);
					alertCreator.setNegativeButton(R.string.no_location_permission_negative, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
					alertCreator.setPositiveButton(R.string.no_location_permission_positive, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
						}
					});
					alertCreator.show();
				}
				return;
			}
		}
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
	}
	
	@Override
	public void onConnectionSuspended(int cause) {
		Log.i(TAG, "Connection suspended");
		mGoogleApiClient.connect();
	}
}
