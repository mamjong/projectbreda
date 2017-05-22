package nl.gemeente.breda.bredaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import nl.gemeente.breda.bredaapp.api.ImageLoader;
import nl.gemeente.breda.bredaapp.domain.Report;
import static nl.gemeente.breda.bredaapp.fragment.MainScreenListFragment.EXTRA_REPORT;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailedReportActivity extends AppCompatActivity {
	
	private static final String TAG = "DetailedReportActivity";
	private TextView description, category;
	private ImageView mediaUrl;
	private Button extraReport;
	private boolean isPressed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailed_report);
		
		description = (TextView) findViewById(R.id.DetailedReportActivity_tv_kindOfDefect);
		mediaUrl = (ImageView) findViewById(R.id.DetailedReportActivity_iv_image);
		extraReport = (Button) findViewById(R.id.DetailedReportActivity_bt_extraReportBtn);
		category = (TextView) findViewById(R.id.DetailedReportActivity_tv_categoryInput);
		
		Bundle extras = getIntent().getExtras();
		
		final Report r = (Report) extras.getSerializable(EXTRA_REPORT);
		
		category.setText(r.getServiceName());
		
		description.setText(r.getDescription());
		new ImageLoader(mediaUrl).execute(r.getMediaUrl());
		
		mediaUrl.setOnClickListener(new View.OnClickListener() {
	
		@Override
		public void onClick(View v) {
			Log.i(TAG, "onClick geactiveerd.");
//			setContentView(R.layout.activity_detailed_report_fullscreen_image);
//			mediaUrl = (ImageView) findViewById(R.id.DetailedReportActivityFullscreenImage_IV_Image);
//			new ImageLoader(mediaUrl).execute(r.getMediaUrl());
//			Toast.makeText(getApplicationContext(), "FULLSCREEN!", Toast.LENGTH_LONG).show();
			Intent fullscreenImageIntent = new Intent(getApplicationContext(), DetailedReportActivityImage.class);
			fullscreenImageIntent.putExtra(EXTRA_REPORT, r);
			startActivity(fullscreenImageIntent);
		}
		
		});
		
		extraReport.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View v) {
				Log.i("Detailed report", "Extra report button clicked");
				Log.i("isPressed: ", String.valueOf(isPressed));
				if(isPressed == false){
					extraReport.setBackgroundResource(R.drawable.onimage2);
					isPressed = true;
					Log.i("isPressedAfterClick: ", String.valueOf(isPressed));
				} else if(isPressed == true){
					extraReport.setBackgroundResource(R.drawable.offimage);
					isPressed = false;
				}
			}
		});
	}
}
