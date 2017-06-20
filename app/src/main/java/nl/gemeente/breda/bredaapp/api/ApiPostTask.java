package nl.gemeente.breda.bredaapp.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import nl.gemeente.breda.bredaapp.util.DatabaseHandler;

public class ApiPostTask extends AsyncTask<String, Void, Void> {
	
	private Context context;
	private String urlBase;
	private String urlString;
	
	public ApiPostTask(Context context, String urlBase, String urlString) {
		this.context = context;
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
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream is = connection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				DatabaseHandler dbh = new DatabaseHandler(context, null, null, 1);
				dbh.addReport(getServiceRequestId(sb.toString()));
			}
		} catch (MalformedURLException e) {
			Log.i("PostTask", "error");
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("PostTask", "error");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public int getServiceRequestId(String jsonBody) {
		int ret = 0;
		
		try {
			JSONArray array = new JSONArray(jsonBody);
			
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				
				if (object.has("service_request_id")) {
					try {
						ret = Integer.parseInt(object.getString("service_request_id"));
					} catch (NumberFormatException e) {
						ret = 0;
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
}
