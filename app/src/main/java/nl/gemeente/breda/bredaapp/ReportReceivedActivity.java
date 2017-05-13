package nl.gemeente.breda.bredaapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cdflynn.android.library.checkview.CheckView;

public class ReportReceivedActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_received);
		
		CheckView checkView = (CheckView) findViewById(R.id.ReportReceivedActivity_cv_checkview);
		checkView.check();
		
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
