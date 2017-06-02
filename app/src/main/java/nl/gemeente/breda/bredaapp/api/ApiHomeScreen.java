package nl.gemeente.breda.bredaapp.api;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import nl.gemeente.breda.bredaapp.domain.Report;

public class ApiHomeScreen extends AsyncTask<String, Void, String> {
	
	private Listener listener = null;
	private NumberOfReports numberListener = null;
	
	public ApiHomeScreen(Listener listener, NumberOfReports numberOfReports) {
		this.listener = listener;
		this.numberListener = numberOfReports;
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		BufferedReader reader = null;
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
			Log.e("ERR", e.getLocalizedMessage());
			return null;
		} catch (IOException e) {
			Log.e("ERR", e.getLocalizedMessage());
			return null;
		} catch (Exception e) {
			Log.e("ERR", e.getLocalizedMessage());
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					Log.e("ERR", e.getLocalizedMessage());
				}
			}
		}
		
		return response;
	}
	
	@Override
	protected void onPostExecute(String response) {
		if ((response == null) || (response.equalsIgnoreCase(""))) {
			listener.noConnectionAvailable();
			return;
		}
		
		try {
			JSONArray reports = new JSONArray(response);
			
			numberListener.onNumberOfReportsAvailable(reports.length());
			
			for (int idx = 0; idx < reports.length(); idx++) {
				// Select this product
				JSONObject thisReport = reports.getJSONObject(idx);
				
				// Create new Report
				Report report = new Report();
				
				// Get values
				if (thisReport.has("service_request_id")) {
					String serviceRequestId = thisReport.getString("service_request_id");
					report.setServiceRequestId(serviceRequestId);
				}
				else if (thisReport.has("service_code")) {
					String serviceCode = thisReport.getString("service_code");
					report.setServiceCode(serviceCode);
				}
				else if (thisReport.has("description")) {
					String description = thisReport.getString("description");
					report.setDescription(description);
				}
				else if (thisReport.has("requested_datetime")) {
					String requestedDatetime = thisReport.getString("requested_datetime");
					report.setRequestedDatetime(requestedDatetime);
				}
				else if (thisReport.has("updated_datetime")) {
					String updatedDatetime = thisReport.getString("updated_datetime");
					report.setUpdatedDatetime(updatedDatetime);
				}
				else if (thisReport.has("status")) {
					String status = thisReport.getString("status");
					report.setStatus(status);
				}
				else if (thisReport.has("status_notes")) {
					String statusNotes = thisReport.getString("status_notes");
					report.setStatusNotes(statusNotes);
				}
				else if (thisReport.has("agency_responsible")) {
					String agencyResponsible = thisReport.getString("agency_responsible");
					report.setAgencyResponsible(agencyResponsible);
				}
				else if (thisReport.has("service_name")) {
					String serviceName = thisReport.getString("service_name");
					report.setServiceName(serviceName);
				}
				else if (thisReport.has("lat")) {
					double lat = thisReport.getDouble("lat");
					report.setLatitude(lat);
				}
				else if (thisReport.has("media_url")) {
					String mediaUrl = thisReport.getString("media_url");
					report.setMediaUrl(mediaUrl);
				}
				else if (thisReport.has("long")) {
					double longtitude = thisReport.getDouble("long");
					report.setLongitude(longtitude);
				}
				
				// Callback
				listener.onReportAvailable(report);
			}
		} catch (JSONException e) {
			Log.e("ERR", e.getLocalizedMessage());
		}
	}
	
	public interface Listener {
		void onReportAvailable(Report report);
		
		void noConnectionAvailable();
	}
	
	public interface NumberOfReports {
		void onNumberOfReportsAvailable(int number);
	}
}
