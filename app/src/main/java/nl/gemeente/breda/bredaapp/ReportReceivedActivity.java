package nl.gemeente.breda.bredaapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cdflynn.android.library.checkview.CheckView;

public class ReportReceivedActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_received);
		
		final CheckView checkView = (CheckView) findViewById(R.id.ReportReceivedActivity_cv_checkview);
		final TextView textView = (TextView) findViewById(R.id.ReportReceivedActivity_tv_ReceivedText);
		//checkView.check();
		
		new CountDownTimer(1000, 1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				
			}
			
			@Override
			public void onFinish() {
				checkView.check();
				textView.setText(getResources().getString(R.string.ReportReceivedActivity_tv_ReceivedText));
			}
			
		}.start();
		
		new CountDownTimer(2543, 1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				
			}
			
			@Override
			public void onFinish() {
				
				Intent backToMainScreenIntent = new Intent(getApplicationContext(), MainScreenActivity.class);
				startActivity(backToMainScreenIntent);
				
				finish();
			}
		}.start();
		
		
	}
}
