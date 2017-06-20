package nl.gemeente.breda.bredaapp.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.util.List;
import java.util.Locale;

import nl.gemeente.breda.bredaapp.R;

public class ReverseGeocoder {
	
	private double lat;
	private double lng;
	private String address;
	
	public ReverseGeocoder(double lat, double lon, Context c) {
		this.lat = lat;
		this.lng = lon;
		
		Geocoder geocoder = new Geocoder(c, Locale.getDefault());
		
		try	{
			List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
			
			if(addresses != null && !addresses.isEmpty()) {
				address = addresses.get(0).getAddressLine(0) + " " + addresses.get(0).getAddressLine(1);
				if(address != null) {
					//Log.i("Address: ", address);
				} else{
					address = c.getString(R.string.addressNotFound);
				}
			}
		} catch (Exception e) {
			Log.e("REVERSELOCATION", e.getMessage());
		}
	}
	
	public String getAddress(){
		return address;
	}
}
