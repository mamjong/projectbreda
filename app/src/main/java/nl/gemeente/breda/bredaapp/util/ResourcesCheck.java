package nl.gemeente.breda.bredaapp.util;

import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.net.InetAddress;

public class ResourcesCheck extends AsyncTask<String, Void, String> {
	
	private Listener listener = null;
	
	public ResourcesCheck(Listener listener) {
		this.listener = listener;
	}
	
	@Override
	protected String doInBackground(String... params) {
		boolean isReachable = false;
		try {
			isReachable = InetAddress.getByName(params[0]).isReachable(1500);
		} catch (IOException e) {
			Log.e("RESOURCE ERROR", e.getMessage());
		}
		
		Log.i("isReachable", String.valueOf(isReachable));
		
		return String.valueOf(isReachable);
	}
	
	@Override
	protected void onPostExecute(String response) {
		boolean isReachable;
		
		if (response == "true"){
			isReachable = true;
		} else {
			isReachable = false;
		}
		
		listener.onCheckAvailable(isReachable);
	}
	
	public interface Listener {
		void onCheckAvailable(boolean isAvailable);
	}
}
