package nl.gemeente.breda.bredaapp.util;

import android.os.Bundle;

import com.akexorcist.localizationactivity.LocalizationDelegate;
import com.akexorcist.localizationactivity.OnLocaleChangedListener;

import java.util.Locale;

import nl.gemeente.breda.bredaapp.AppBaseActivity;

public class LocalizationManager extends AppBaseActivity implements OnLocaleChangedListener {
	
	private LocalizationDelegate localizationDelegate = new LocalizationDelegate(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		localizationDelegate.addOnLocaleChangedListener(this);
		localizationDelegate.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		localizationDelegate.onResume();
	}
	
	public final void setLanguage(String language) {
		localizationDelegate.setLanguage(language);
	}
	
	public final void setLanguage(Locale locale) {
		localizationDelegate.setLanguage(locale);
	}
	
	public final void setDefaultLanguage(String language) {
		localizationDelegate.setDefaultLanguage(language);
	}
	
	public final void setDefaultLanguage(Locale locale) {
		localizationDelegate.setDefaultLanguage(locale);
	}
	
	public final String getLanguage() {
		return localizationDelegate.getLanguage();
	}
	
	public final Locale getLocale() {
		return localizationDelegate.getLocale();
	}
	
	// Just override method locale change event
	@Override
	public void onBeforeLocaleChanged() { }
	
	@Override
	public void onAfterLocaleChanged() { }
}
