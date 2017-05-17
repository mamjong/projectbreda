//================================================================================
// This class is made by:
// - Thimo Koolen
//================================================================================
package nl.gemeente.breda.bredaapp;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

import nl.gemeente.breda.bredaapp.eastereggs.TestEasterEgg;
import nl.gemeente.breda.bredaapp.eastereggs.snake.Snake;
import nl.gemeente.breda.bredaapp.eastereggs.spaceinvaders.MainActivity;
import nl.gemeente.breda.bredaapp.eastereggs.spaceinvaders.SpaceInvadersGame;
import nl.gemeente.breda.bredaapp.fragment.MainScreenListFragment;
import nl.gemeente.breda.bredaapp.fragment.MainScreenMapFragment;


public class SplashActivity extends AppCompatActivity {

	private int i;
	private CountDownTimer timer;
	
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
	
	    i = 1;
	    ImageView logo = (ImageView) findViewById(R.id.activitySplashScreen_iv_logo);
	    logo.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
				if(i < 5){
					i++;
				}
				else if(i == 5){
					timer.cancel();

					Random r = new Random();
					int rand = r.nextInt(2) + 1;
					Log.i("RANDOM", "" + rand);
					
					switch (rand) {
						case 1:
							Intent spaceinvaders = new Intent(getApplicationContext(), MainActivity.class);
							startActivity(spaceinvaders);
							break;
						
						case 2:
							Intent snake = new Intent(getApplicationContext(), Snake.class);
							startActivity(snake);
							break;
						
						default:
							Intent easteregg = new Intent(getApplicationContext(), TestEasterEgg.class);
							startActivity(easteregg);
							break;
					}
				}
			    
		    }
	    });
	    
        timer = new CountDownTimer(2543, 1000) {

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
        };
	    
        timer.start();
    }
    
    @Override
	public void onResume(){
	    super.onResume();
	    timer.start();
    }
}