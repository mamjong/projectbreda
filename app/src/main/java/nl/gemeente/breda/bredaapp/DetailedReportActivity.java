package nl.gemeente.breda.bredaapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import nl.gemeente.breda.bredaapp.domain.Report;
import static nl.gemeente.breda.bredaapp.fragment.MainScreenListFragment.EXTRA_REPORT;

public class DetailedReportActivity extends AppCompatActivity {
	
	private TextView description;
	private ImageView mediaUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailed_report);
		
		description = (TextView) findViewById(R.id.DetailedReportActivity_tv_kindOfDefect);
		mediaUrl = (ImageView) findViewById(R.id.DetailedReportActivity_IV_Image);
		
		Bundle extras = getIntent().getExtras();
		
		Report r = (Report) extras.getSerializable(EXTRA_REPORT);
		
		description.setText(r.getDescription());
		new ImageLoader(mediaUrl).execute(r.getMediaUrl());
	}
	
	/**
	 * Interne asynchrone class om afbeeldingen te laden.
	 */
	private class ImageLoader extends AsyncTask<String, Void, Bitmap> {
		ImageView imageView;
		
		public ImageLoader(ImageView imageView) {
			this.imageView = imageView;
		}
		
		protected Bitmap doInBackground(String... urls) {
			String imageURL = urls[0];
			Bitmap bitmap = null;
			try {
				InputStream in = new java.net.URL(imageURL).openStream();
				bitmap = BitmapFactory.decodeStream(in);
				
			} catch (Exception e) {
				Log.e("Error Message", "No image possible");
				e.printStackTrace();
			}
			return bitmap;
		}
		
		protected void onPostExecute(Bitmap result) {
			imageView.setImageBitmap(result);
		}
	}
}
