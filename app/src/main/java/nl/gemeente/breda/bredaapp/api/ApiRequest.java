package nl.gemeente.breda.bredaapp.api;

import android.content.Context;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.gemeente.breda.bredaapp.businesslogic.ServiceManager;
import nl.gemeente.breda.bredaapp.domain.Service;
import nl.gemeente.breda.bredaapp.util.Converter;
import nl.gemeente.breda.bredaapp.util.ReverseGeocoder;

public class ApiRequest {
	
	private Context context;
	
	public ApiRequest(Context context) {
		this.context = context;
	}
	
	public void post(String service, String description, double latitude, double longitude) {
		String serviceCode = "";
		for (Service s : ServiceManager.getServices()) {
			if (s.getServiceName().equalsIgnoreCase(service)) {
				serviceCode = s.getServiceCode();
			}
		}
		
		ReverseGeocoder reverseGeocoder = new ReverseGeocoder(latitude, longitude, context);
		String[] addressComponents = reverseGeocoder.getAddress().split(" ");
		String address = addressComponents[0];
		//String addressID = addressComponents[1];
		int addressID = 0;
		for (String s : addressComponents) {
			if (Converter.isInt(s)) {
				addressID = Integer.parseInt(s);
				return;
			}
		}
		
		String urlBase = "http://37.34.59.50/breda/CitySDK/requests.json";
		
		StringBuilder urlStringBuilder = new StringBuilder();
		urlStringBuilder.append("?service_code=\"" + serviceCode + "\"");
		urlStringBuilder.append("&description=\"" + description + "\"");
		urlStringBuilder.append("&lat=" + latitude);
		urlStringBuilder.append("&long=" + longitude);
		urlStringBuilder.append("&address_string=\"" + address + "\"");
		urlStringBuilder.append("&address_id=" + addressID);
		
		String urlString = urlStringBuilder.toString();
		
		
		new ApiPostTask(urlBase, urlString).execute();
	}
}
