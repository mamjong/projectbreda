package nl.gemeente.breda.bredaapp.api;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class ImageLoader extends AsyncTask<String, Void, Bitmap> {
	ImageView imageView;
	
	public ImageLoader(ImageView imageView) {
		this.imageView = imageView;
	}
	
	protected Bitmap doInBackground(String... urls) {
		String imageURL = urls[0];
		if (imageURL == null) {
			return null;
		}
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
		if (result == null) {
			return;
		}
		imageView.setImageBitmap(result);
	}
}