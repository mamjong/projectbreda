package nl.gemeente.breda.bredaapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckDataActivity extends AppCompatActivity {
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_data);
		
		Button confirmBtn = (Button) findViewById(R.id.CheckDataActivity_bt_confirmReportButton);
		
		Intent i =  getIntent();
		Bundle extras = getIntent().getExtras();
		
		ImageView itemImageView = (ImageView) findViewById(R.id.CheckDataActivity_iv_defectImage);
		TextView serviceTypeInput = (TextView) findViewById(R.id.CheckDataActivity_tv_categoryInput);
		Bitmap inputImage;
		
		if ( getIntent().hasExtra("IMAGE") ) {
			inputImage = BitmapFactory.decodeByteArray(
					i.getByteArrayExtra("IMAGE"), 0, getIntent().getByteArrayExtra("IMAGE").length);
			itemImageView.setImageBitmap(inputImage);
		}
		
		if ( i.hasExtra("SERVICE") ){
			serviceTypeInput.setText(extras.getString("SERVICE"));
		}
		
		confirmBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Intent confirmScreenIntent = new Intent(CheckDataActivity.this, ReportReceivedActivity.class);
				startActivity(confirmScreenIntent);
				finish();
			}
		});
		
		
	}
}
