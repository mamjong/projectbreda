package nl.gemeente.breda.bredaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Arrays;

import nl.gemeente.breda.bredaapp.domain.User;
import nl.gemeente.breda.bredaapp.util.ThemeManager;

public class UserSettingsActivity extends AppBaseActivity {
	
	private User user;
	private EditText currentEmail;
	private Switch changeSettings;
	private Spinner themeSpinner;
	private int reportRadius, seekBarPos;
	private TextView reportRadiusView;
	public static final String PREFS_NAME = "PrefsFile";
	private SeekBar changeRadius;
	private String[] themeSpinnerEntries;
	private String selectedTheme;
	
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
		changeSettings = (Switch) findViewById(R.id.UserSettingsActivity_sw_ChangeSettings);
		changeRadius = (SeekBar) findViewById(R.id.UserSettingsActivity_sb_ChangeRadius);
		themeSpinner = (Spinner) findViewById(R.id.UserSettingsActivity_sp_ChangeTheme);
		reportRadiusView = (TextView) findViewById(R.id.UserSettingsActivity_tv_currentRadius);
		reportRadiusView.setText(reportRadius + " meters");
		
		this.themeSpinnerEntries = getResources().getStringArray(R.array.themeSpinner);
		
		int selectedThemeIndex = 0;
		if (selectedTheme.equals("standard")) {
			selectedThemeIndex = Arrays.asList(themeSpinnerEntries).indexOf(getResources().getString(R.string.themeStandard));
			Log.i("Selected theme index", "standard: " + selectedThemeIndex);
		} else if (selectedTheme.equals("night")) {
			selectedThemeIndex = Arrays.asList(themeSpinnerEntries).indexOf(getResources().getString(R.string.themeNight));
			Log.i("Selected theme index", "night: " +selectedThemeIndex);
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout_custom_row, themeSpinnerEntries);
		themeSpinner.setAdapter(adapter);
		themeSpinner.setEnabled(false);
		themeSpinner.setSelection(selectedThemeIndex);
		
		currentEmail.setEnabled(false);
		
		user = dbh.getUser();
		currentEmail.setText(user.getMailAccount());
		
		changeRadius.setProgress(seekBarPos);
		changeRadius.setEnabled(false);
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
					editor.putString("theme", "night");
					editor.commit();
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		
		changeSettings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				currentEmail.setEnabled(isChecked);
				changeRadius.setEnabled(isChecked);
				themeSpinner.setEnabled(isChecked);
				
				if (isChecked) {
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
				} else {
					//User
					dbh.updateUser(currentEmail.getText().toString());
					user = dbh.getUser();
					currentEmail.setText(user.getMailAccount());
					
					//Radius
					SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
					editor.putInt("ReportRadius", reportRadius);
					editor.putInt("SeekBarPos", seekBarPos);
					editor.commit();
				}
			}
		});
		
		changeRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				reportRadius = progress * 100 + 100;
				reportRadiusView.setText(reportRadius + " meters");
				seekBarPos = progress;
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
