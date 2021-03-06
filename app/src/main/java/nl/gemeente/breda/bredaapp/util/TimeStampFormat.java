package nl.gemeente.breda.bredaapp.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStampFormat {
	
	private String formatDateTime;
	
	// Format: 2017-05-17T20:50:27+03:00 van Helsinki
//	private SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	
	// Format 0001-01-01T00:00:00 van Gemeente Breda
	private SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	// Format example = 10-10-1010 om 10:10
//	private SimpleDateFormat reqDateFormat = new SimpleDateFormat("dd-MM-YYYY 'om' HH:mm");
	
	// Format example = 10-10-1010
	private SimpleDateFormat reqDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	public String convertTimeStamp(String formatDateTime) {
		Date date;
		try {
			date = sourceFormat.parse(formatDateTime);
			formatDateTime = reqDateFormat.format(date);
		} catch (ParseException e) {
			Log.i("Error", e.getMessage());
		}
		return formatDateTime;
	}
	
	public String getTime() {
		return formatDateTime;
	}
	
	public void setTime(String formatDateTime) {
		this.formatDateTime = formatDateTime;
	}
}

