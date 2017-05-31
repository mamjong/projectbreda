//================================================================================
// This class is made by:
// - Thimo Koolen
//================================================================================
package nl.gemeente.breda.bredaapp;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import nl.gemeente.breda.bredaapp.api.ApiServices;
import nl.gemeente.breda.bredaapp.businesslogic.ServiceManager;
import nl.gemeente.breda.bredaapp.domain.Service;


public class SplashActivity extends AppCompatActivity implements ApiServices.Listener {
	
	private CountDownTimer timer;
	private ApiServices apiServices;
	
	//================================================================================
	// Mutators
	//================================================================================
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		Context context = this;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
		
		TextView no_wifi = (TextView) findViewById(R.id.no_wifi);
		no_wifi.setVisibility(View.INVISIBLE);
		
		if((activeNetwork == null) || !(activeNetwork.isConnected())){
			Intent noInternet = new Intent(getApplicationContext(), NoInternet.class);
			noInternet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(noInternet);
		}
		
		if (activeNetwork.getType() != ConnectivityManager.TYPE_WIFI) {
			no_wifi.setVisibility(View.VISIBLE);
		}
		
		PackageInfo packageInfo = null;
		
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String version = "";
		
		if (packageInfo != null) {
			version = packageInfo.versionName;
		} else {
			version = getResources().getString(R.string.activitySplashScreen_text_unknownVersion);
		}
		
		apiServices = new ApiServices(this);
		getServices();
		
		TextView appVersion = (TextView) findViewById(R.id.activitySplashScreen_tv_appVersion);
		appVersion.setText(getResources().getString(R.string.activitySplashScreen_tv_appVersion) + " " + version);
		
		ProgressBar pb = (ProgressBar) findViewById(R.id.activitySplashScreen_pb_loader);
		pb.getIndeterminateDrawable().setColorFilter(Color.parseColor("#d91d49"), android.graphics.PorterDuff.Mode.SRC_ATOP);
		
		timer = new CountDownTimer(10000, 250) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				Log.i("LOADING", millisUntilFinished + ":" + apiServices.getStatus());
				if (apiServices.getStatus() == AsyncTask.Status.FINISHED) {
					finishSplashScreen();
					timer.cancel();
				}
			}
			
			@Override
			public void onFinish() {
				if (apiServices.getStatus() == AsyncTask.Status.FINISHED) {
					finishSplashScreen();
					timer.cancel();
				} else {
					timer.start();
					Log.i("RESTART", "Restart timer");
				}
			}
		};
		
		timer.start();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		timer.start();
	}
	
	@Override
	public void onServiceAvailable(Service service) {
		Log.i("Service", service.getServiceName());
		ServiceManager.addService(service);
	}
	
	public void getServices() {
		ServiceManager.emptyArray();
		String[] urls = new String[]{"https://asiointi.hel.fi/palautews/rest/v1/services.json"};
		apiServices.execute(urls);
	}
	
	public void finishSplashScreen() {
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext(), null, null, 1);
		
		Intent returnUser = new Intent(getApplicationContext(), MainScreenActivity.class);
		Intent newUser = new Intent(getApplicationContext(), AddEmailActivity.class);
		returnUser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		newUser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		if (dbh.checkUser()) {
			startActivity(returnUser);
		} else {
			startActivity(newUser);
		}
		finish();
	}
}