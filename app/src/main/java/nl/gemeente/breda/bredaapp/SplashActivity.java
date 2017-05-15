//================================================================================
// This class is made by:
// - Thimo Koolen
//================================================================================
package nl.gemeente.breda.bredaapp;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import nl.gemeente.breda.bredaapp.domain.User;
import nl.gemeente.breda.bredaapp.testing.LocationActivity;


public class SplashActivity extends AppCompatActivity {

    //================================================================================
    // Mutators
    //================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
	
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
    
        TextView appVersion = (TextView) findViewById(R.id.activitySplashScreen_tv_appVersion);
	    appVersion.setText(getResources().getString(R.string.activitySplashScreen_tv_appVersion) + " " + version);

        ProgressBar pb = (ProgressBar) findViewById(R.id.activitySplashScreen_pb_loader);
        pb.getIndeterminateDrawable().setColorFilter(Color.parseColor("#d91d49"), android.graphics.PorterDuff.Mode.SRC_ATOP);

        new CountDownTimer(2543, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

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
        }.start();
    }
}