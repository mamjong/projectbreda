package nl.gemeente.breda.bredaapp.api;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiPostTask extends AsyncTask<String, Void, String> {
	
	private String urlBase;
	private String urlString;
	
	public ApiPostTask(String urlBase, String urlString) {
		this.urlBase = urlBase;
		this.urlString = urlString;
	}
	
	@Override
	protected String doInBackground(String... params) {
		try {
			Log.i("PostTask", "posted 1");
			URL url = new URL(urlBase + urlString);
			Log.i("PostTask 2", url.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlString.getBytes().length));
			connection.setUseCaches(false);

			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlString);

			wr.flush();
			wr.close();

			Log.i("PostTask", "posted");
		} catch (MalformedURLException e) {
			Log.i("PostTask", "error");
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("PostTask", "error");
			e.printStackTrace();
		}
		
		return null;
	}
}
