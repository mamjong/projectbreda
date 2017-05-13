package nl.gemeente.breda.bredaapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ReportReceivedActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_received);
		
		new CountDownTimer(2543, 1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				
			}
			
			@Override
			public void onFinish() {
				
				Intent backToMainScreenIntent = new Intent(getApplicationContext(), SplashActivity.class);
				startActivity(backToMainScreenIntent);
				
				finish();
			}
		}.start();
		
		
	}
}
