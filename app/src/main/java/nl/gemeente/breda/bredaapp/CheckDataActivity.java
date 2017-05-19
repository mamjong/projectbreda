package nl.gemeente.breda.bredaapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import nl.gemeente.breda.bredaapp.domain.Report;

import static nl.gemeente.breda.bredaapp.fragment.MainScreenListFragment.EXTRA_REPORT;

public class CheckDataActivity extends AppCompatActivity {
	
	private Bitmap bitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_data);
		
		final Button confirmBtn = (Button) findViewById(R.id.CheckDataActivity_bt_confirmReportButton);
		final ImageView itemImageView = (ImageView) findViewById(R.id.CheckDataActivity_iv_defectImage);
		TextView serviceTypeInput = (TextView) findViewById(R.id.CheckDataActivity_tv_categoryInput);
		
		Intent i =  getIntent();
		Bundle extras = getIntent().getExtras();
		
		bitmap = loadBitmap(CheckDataActivity.this, "inframeld.jpeg");
		itemImageView.setImageBitmap(bitmap);
//		Bitmap inputImage;
		
		final Report r = (Report) extras.getSerializable(EXTRA_REPORT);
		
//		if ( getIntent().hasExtra("IMAGE") ) {
//			inputImage = BitmapFactory.decodeByteArray(
//					i.getByteArrayExtra("IMAGE"), 0, getIntent().getByteArrayExtra("IMAGE").length);
//			itemImageView.setImageBitmap(inputImage);
//		}
		
		if ( i.hasExtra("SERVICE") ){
			serviceTypeInput.setText(extras.getString("SERVICE"));
		}
		
		itemImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent fullscreenCheckDataImageIntent = new Intent(getApplicationContext(), CheckDataImageActivity.class);
				startActivity(fullscreenCheckDataImageIntent);
			}
		});
		
		confirmBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Intent confirmScreenIntent = new Intent(CheckDataActivity.this, ReportReceivedActivity.class);
				confirmScreenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(confirmScreenIntent);
				finish();
			}
		});
	}
	
	private Bitmap loadBitmap(Context context, String name) {
		Bitmap bitmap = null;
		FileInputStream fis;
		
		try {
			fis = context.openFileInput(name);
			bitmap = BitmapFactory.decodeStream(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bitmap;
	}
}
