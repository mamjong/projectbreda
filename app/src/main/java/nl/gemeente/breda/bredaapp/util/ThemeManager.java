package nl.gemeente.breda.bredaapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import nl.gemeente.breda.bredaapp.R;

public class ThemeManager {
	public static final int THEME_STANDARD = 0;
	public static final int THEME_DARK = 1;
	
	public static void setTheme(Activity activity) {
//		switch (theme) {
//			case THEME_DEFAULT:
//				activity.setTheme(R.style.AppTheme);
//				break;
//			
//			case THEME_DARK:
//				activity.setTheme(R.style.AppThemeNight);
//				break;
//		}
		
		SharedPreferences pref = activity.getSharedPreferences("PrefsFile", Context.MODE_PRIVATE);
		String savedTheme = pref.getString("theme", "standard");
		Log.i("Got theme", savedTheme);
		
		if (savedTheme.equalsIgnoreCase("standard")) {
			activity.setTheme(R.style.AppTheme);
			Log.i("Theme", "Standard");
		} else if (savedTheme.equalsIgnoreCase("night")) {
			activity.setTheme(R.style.AppThemeNight);
			Log.i("Theme", "Night");
		}
	}
}
