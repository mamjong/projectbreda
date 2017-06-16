package nl.gemeente.breda.bredaapp.api;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiPostTask extends AsyncTask<String, Void, Void> {
	
	private String urlBase;
	private String urlString;
	
	public ApiPostTask(String urlBase, String urlString) {
		this.urlBase = urlBase;
		this.urlString = urlString;
	}
	
	@Override
	protected Void doInBackground(String... params) {
		try {
			String urlFull = urlBase + "?" + urlString;
			URL url = new URL(urlFull);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setFixedLengthStreamingMode(urlString.getBytes().length);
			PrintWriter out = new PrintWriter(connection.getOutputStream());
			out.print(urlString);
			out.flush();
			out.close();
			
			connection.connect();
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
