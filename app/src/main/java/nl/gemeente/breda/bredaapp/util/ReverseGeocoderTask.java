package nl.gemeente.breda.bredaapp.util;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import nl.gemeente.breda.bredaapp.domain.Report;

public class ReverseGeocoderTask extends AsyncTask<Void, Void, String> {
	
	private Context context;
	private Report report;
	private ReverseGeoCoderListener listener;
	
	public ReverseGeocoderTask(Report report, Context context, ReverseGeoCoderListener listener) {
		this.context = context;
		this.report = report;
		this.listener = listener;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(Void... params) {
		ReverseGeocoder reverseGeocoder = new ReverseGeocoder(report.getLatitude(), report.getLongitude(), context);
		
		return reverseGeocoder.getAddress();
	}
	
	@Override
	protected void onPostExecute(String s) {
		super.onPostExecute(s);
		listener.onAddressAvailable(report, s);
	}
	
	public interface ReverseGeoCoderListener {
		void onAddressAvailable(Report report, String address);
	}
}
