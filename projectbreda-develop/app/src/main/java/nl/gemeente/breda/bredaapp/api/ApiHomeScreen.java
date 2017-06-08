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

import nl.gemeente.breda.bredaapp.domain.Report;

public class ApiHomeScreen extends AsyncTask<String, Void, String> {

    private Listener listener = null;

    public ApiHomeScreen(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        BufferedReader reader = null;
        String urlString = "";
        String response = "";

        try{
            URL url = new URL(params[0]);
            URLConnection connection = url.openConnection();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while( ( line = reader.readLine() ) != null){
                response += line;
            }
        } catch (MalformedURLException e) {
            Log.e("ERR", e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e("ERR", e.getLocalizedMessage());
            return null;
        } catch (Exception e){
            Log.e("ERR", e.getLocalizedMessage());
            return null;
        } finally {
            if (reader != null){
                try{
                    reader.close();
                } catch (IOException e) {
                    Log.e("ERR", e.getLocalizedMessage());
                    return null;
                }
            }
        }

        return response;
    }

    protected void onPostExecute(String response){

        Log.i("RESPONSE", response);

        try {
            JSONArray reports = new JSONArray(response);

            for (int idx = 0; idx < reports.length(); idx++){
                // Select this product
                JSONObject thisReport = reports.getJSONObject(idx);

                // Create new Report
                Report report = new Report();

                // Get values
                if(thisReport.has("service_request_id")) {
                    String service_request_id = thisReport.getString("service_request_id");
                    report.setServiceRequestId(service_request_id);
                }
                if(thisReport.has("service_code")) {
                    String service_code = thisReport.getString("service_code");
                    report.setServiceCode(service_code);
                }
                if(thisReport.has("description")) {
                    String description = thisReport.getString("description");
                    report.setDescription(description);
                }
                if(thisReport.has("requested_datetime")) {
                    String requested_datetime = thisReport.getString("requested_datetime");
                    report.setRequestedDatetime(requested_datetime);
                }
                if(thisReport.has("updated_datetime")) {
                    String updated_datetime = thisReport.getString("updated_datetime");
                    report.setUpdatedDatetime(updated_datetime);
                }
                if(thisReport.has("status")) {
                    String status = thisReport.getString("status");
                    report.setStatus(status);
                }
                if(thisReport.has("status_notes")) {
                    String status_notes = thisReport.getString("status_notes");
                    report.setStatusNotes(status_notes);
                }
                if(thisReport.has("agency_responsible")) {
                    String agency_responsible = thisReport.getString("agency_responsible");
                    report.setAgencyResponsible(agency_responsible);
                }
                if(thisReport.has("service_name")) {
                    String service_name = thisReport.getString("service_name");
                    report.setServiceName(service_name);
                }
                if(thisReport.has("lat")) {
                    double lat = thisReport.getDouble("lat");
                    report.setLatitude(lat);
                }
                if(thisReport.has("media_url")) {
                    String media_url = thisReport.getString("media_url");
                    report.setMediaUrl(media_url);
                }
                if(thisReport.has("long")) {
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
    }
}
