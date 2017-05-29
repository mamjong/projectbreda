package nl.gemeente.breda.bredaapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.lang.reflect.GenericArrayType;
import java.util.Arrays;

import nl.gemeente.breda.bredaapp.domain.User;

public class UserSettingsActivity extends AppBaseActivity {
	
	public static final String PREFS_NAME = "PrefsFile";
	private User user;
	private Button changeEmailButton;
	private EditText currentEmail;
	private Switch changeSettings;
	private Spinner themeSpinner, languageSpinner;
	private int reportRadius, seekBarPos;
	private TextView reportRadiusView;
	private SeekBar changeRadius;
	private String[] themeSpinnerEntries, languageSpinnerEntries;
	private String selectedTheme, selectedLanguage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setMenuSelected(getIntent().getExtras());
		super.setToolbarTitle(R.string.UserSettingsAcitivity_name);
		setContentView(R.layout.activity_user_settings);
		
		SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		reportRadius = preferences.getInt("ReportRadius", 500);
		seekBarPos = preferences.getInt("SeekBarPos", 4);
		selectedTheme = preferences.getString("theme", "standard");
		Log.i("Settings loaded", selectedTheme);
		
		
		final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext(), null, null, 1);
		currentEmail = (EditText) findViewById(R.id.UserSettingsActivity_et_currentEmail);
		changeRadius = (SeekBar) findViewById(R.id.UserSettingsActivity_sb_ChangeRadius);
		themeSpinner = (Spinner) findViewById(R.id.UserSettingsActivity_sp_ChangeTheme);
		languageSpinner = (Spinner) findViewById(R.id.UserSettingsActivity_sp_ChangeLanguage);
		reportRadiusView = (TextView) findViewById(R.id.UserSettingsActivity_tv_currentRadius);
		changeEmailButton = (Button) findViewById(R.id.UserSettingsActivity_btn_confirmEmail);
		reportRadiusView.setText(reportRadius + " meters");
		
		
		
		this.themeSpinnerEntries = getResources().getStringArray(R.array.themeSpinner);
		languageSpinnerEntries = getResources().getStringArray(R.array.spinnerLanguageData);
		
		int selectedThemeIndex = 0;
		if (selectedTheme.equals("standard")) {
			selectedThemeIndex = Arrays.asList(themeSpinnerEntries).indexOf("standard");
			Log.i("Selected theme index", "standard: " + selectedThemeIndex);
		} else if (selectedTheme.equals("dark")) {
			selectedThemeIndex = Arrays.asList(themeSpinnerEntries).indexOf("dark");
			Log.i("Selected theme index", "night: " + selectedThemeIndex);
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout_custom_row, themeSpinnerEntries);
		themeSpinner.setAdapter(adapter);
		themeSpinner.setSelection(selectedThemeIndex);
		
		ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout_custom_row, languageSpinnerEntries);
		languageSpinner.setAdapter(languageAdapter);
		
		
		
		currentEmail.setEnabled(false);
		
		user = dbh.getUser();
		currentEmail.setText(user.getMailAccount());
		
		changeRadius.setProgress(seekBarPos);
		changeRadius.setMax(19);
		
		themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String selected = parent.getItemAtPosition(position).toString();
				
				if (selected.equals(getResources().getString(R.string.themeStandard))) {
					SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
					editor.putString("theme", "standard");
					editor.commit();
				} else if (selected.equals(getResources().getString(R.string.themeNight))) {
					SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
					editor.putString("theme", "dark");
					editor.commit();
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		changeEmailButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dbh.updateUser(currentEmail.getText().toString());
				user = dbh.getUser();
				currentEmail.setText(user.getMailAccount());
			}
		});
		
		languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String selected = parent.getItemAtPosition(position).toString();
				
				if (selected.equals("Nederlands")) {
					SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
					editor.putString("language", "nl");
					editor.commit();
				} else if (selected.equals("Engels")) {
					SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
					editor.putString("language", "en");
					editor.commit();
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent){
				
			}
		});
		
		
		currentEmail.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
					changeSettings.setEnabled(true);
				} else {
					changeSettings.setEnabled(false);
				}
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		changeRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				reportRadius = progress * 100 + 100;
				reportRadiusView.setText(reportRadius + " meters");
				seekBarPos = progress;
				SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
				editor.putInt("ReportRadius", reportRadius);
				editor.putInt("SeekBarPos", seekBarPos);
				editor.commit();
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
		});
	}
}
