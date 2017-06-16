package nl.gemeente.breda.bredaapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;

import nl.gemeente.breda.bredaapp.api.ApiRequest;
import nl.gemeente.breda.bredaapp.domain.Report;

import static nl.gemeente.breda.bredaapp.fragment.MainScreenListFragment.EXTRA_REPORT;

public class CheckDataActivity extends AppBaseActivity {
	
	private double latitude = 0.D;
	private double longitude = 0.D;
	
	protected static Bitmap loadBitmap(Context context, String name) {
		Bitmap bitmap = null;
		FileInputStream fis;
		
		try {
			fis = context.openFileInput(name);
			bitmap = BitmapFactory.decodeStream(fis);
			fis.close();
		} catch (IOException e) {
			Log.e("ERR", String.valueOf(e));
		}
		
		return bitmap;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_data);
		super.setMenuSelected(getIntent().getExtras());
		super.setShareVisible(false);
		
		final Button confirmBtn = (Button) findViewById(R.id.CheckDataActivity_bt_confirmReportButton);
		final ImageView itemImageView = (ImageView) findViewById(R.id.CheckDataActivity_iv_defectImage);
		final TextView serviceTypeInput = (TextView) findViewById(R.id.CheckDataActivity_tv_categoryInput);
		final TextView commentsInput = (TextView) findViewById(R.id.CheckDataActivity_tv_comment); 
		
		Intent i = getIntent();
		Bundle extras = getIntent().getExtras();
		
		Bitmap bitmap = loadBitmap(CheckDataActivity.this, "inframeld.jpeg");
		itemImageView.setImageBitmap(bitmap);
		
		final Report r = (Report) extras.getSerializable(EXTRA_REPORT);
		
		if (i.hasExtra("SERVICE")) {
			serviceTypeInput.setText(extras.getString("SERVICE"));
		}
		
		if (i.hasExtra("COMMENT")) {
			commentsInput.setText(extras.getString("COMMENT"));
		}
		
		if (i.hasExtra("LATITUDE")) {
			latitude = extras.getDouble("LATITUDE");
		}
		
		if (i.hasExtra("LONGITUDE")) {
			longitude = extras.getDouble("LONGITUDE");
		}
		
		itemImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent fullscreenCheckDataImageIntent = new Intent(getApplicationContext(), CheckDataImageActivity.class);
				fullscreenCheckDataImageIntent.putExtra(EXTRA_REPORT, r);
				startActivity(fullscreenCheckDataImageIntent);
			}
		});
		
		confirmBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("Confirm Button", "Clicked");
				ApiRequest req = new ApiRequest(CheckDataActivity.this);
				req.post(serviceTypeInput.getText().toString(), commentsInput.getText().toString(), latitude, longitude);
				
				Intent confirmScreenIntent = new Intent(CheckDataActivity.this, ReportReceivedActivity.class);
				confirmScreenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(confirmScreenIntent);
				finish();
			}
		});
	}
}
