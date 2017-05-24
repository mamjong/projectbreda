package nl.gemeente.breda.bredaapp.api;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import nl.gemeente.breda.bredaapp.domain.Service;

public class ApiServices extends AsyncTask<String, Void, String> {
	
	private Listener listener = null;
	
	public ApiServices(Listener listener) {
		this.listener = listener;
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		InputStream inputStream = null;
		BufferedReader reader = null;
		String urlString = "";
		String response = "";
		
		try {
			URL url = new URL(params[0]);
			URLConnection connection = url.openConnection();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				response += line;
			}
		} catch (MalformedURLException e) {
			Log.e("ERROR", e.getLocalizedMessage());
			return null;
		} catch (IOException e) {
			Log.e("ERROR", e.getLocalizedMessage());
			return null;
		} catch (Exception e) {
			Log.e("ERROR", e.getLocalizedMessage());
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					Log.e("ERROR", e.getLocalizedMessage());
					return null;
				}
			}
		}
		
		return response;
	}
	
	protected void onPostExecute(String response) {
		if ((response == null) || (response.equalsIgnoreCase(""))) {
			return;
		}
		
		//Log.i("RESPONSE", response);
		
		try {
			JSONArray services = new JSONArray(response);
			
			for (int idx = 0; idx < services.length(); idx++) {
				// Select this product
				JSONObject thisService = services.getJSONObject(idx);
				
				// Create new Report
				Service service = new Service();
				
				// Get values
				if (thisService.has("service_code")) {
					String service_code = thisService.getString("service_code");
					service.setServiceCode(service_code);
				}
				if (thisService.has("service_name")) {
					String service_name = thisService.getString("service_name");
					service.setServiceName(service_name);
				}
				if (thisService.has("description")) {
					String description = thisService.getString("description");
					service.setDescription(description);
				}
				if (thisService.has("metadata")) {
					boolean metadata = thisService.getBoolean("metadata");
					service.setMetadata(metadata);
				}
				if (thisService.has("type")) {
					String type = thisService.getString("type");
					service.setType(type);
				}
				if (thisService.has("keywords")) {
					String keywords = thisService.getString("keywords");
					service.setKeywords(keywords);
				}
				if (thisService.has("group")) {
					String group = thisService.getString("group");
					service.setGroup(group);
				}
				
				// Callback
				listener.onServiceAvailable(service);
			}
		} catch (JSONException e) {
			Log.e("ERROR", e.getLocalizedMessage());
		}
	}
	
	public interface Listener {
		void onServiceAvailable(Service service);
	}
}
