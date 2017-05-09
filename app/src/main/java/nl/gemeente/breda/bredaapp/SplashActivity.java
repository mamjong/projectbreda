//================================================================================
// This class is made by:
// - Thimo Koolen
//================================================================================
package nl.gemeente.breda.bredaapp;


import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import nl.gemeente.breda.bredaapp.testing.LocationActivity;


public class SplashActivity extends AppCompatActivity {

    //================================================================================
    // Mutators
    //================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext(), null, null, 1);

        getSupportActionBar().hide();


        setContentView(R.layout.activity_splash);

        ProgressBar pb = (ProgressBar) findViewById(R.id.activitySplashScreen_pb_loader);

        pb.getIndeterminateDrawable().setColorFilter(Color.parseColor("#d91d49"), android.graphics.PorterDuff.Mode.SRC_ATOP);

        new CountDownTimer(2543, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Intent returnUser = new Intent(getApplicationContext(), MainScreenActivity.class);
                Intent newUser = new Intent(getApplicationContext(), AddEmailActivity.class);
                returnUser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                if (dbh.checkUser() == true ) {
                    startActivity(returnUser);
                } else {
                    startActivity(newUser);
                }
                finish();
            }
        }.start();
    }
}