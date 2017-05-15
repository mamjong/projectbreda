package nl.gemeente.breda.bredaapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.domain.Report;

public class ReportAdapter extends ArrayAdapter<Report> {
	
	public ReportAdapter(Context context, ArrayList<Report> reports) {
		super(context, R.layout.fragment_list_view_row, reports);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// Create report
		Report report = getItem(position);
		
		// Check for existing view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_list_view_row, parent, false);
		}
			// Select row items
			ImageView mediaUrl = (ImageView) convertView.findViewById(R.id.fragmentListViewRow_IV_mediaUrl);
			TextView description = (TextView) convertView.findViewById(R.id.fragmentListViewRow_TV_description);
			TextView status = (TextView) convertView.findViewById(R.id.fragmentListViewRow_TV_status);
			
			// Get and set content
//			description.setText(report.getDescription());
			description.setText("subCategoryDescription");
			status.setText(report.getStatus());
			new ImageLoader(mediaUrl).execute(report.getMediaUrl());
			
			// Return view
			return convertView;
		
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