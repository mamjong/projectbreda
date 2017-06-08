package nl.gemeente.breda.bredaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import nl.gemeente.breda.bredaapp.domain.User;
public class UserSettingsActivity extends AppBaseActivity {
	
	public static final String PREFS_NAME = "PrefsFile";
	private User user;
	private EditText currentEmail;
	private int reportRadius;
	private int seekBarPos;
	private String toastEmailUpdated;
	private boolean initialStart;
	private static final String TAG = "Usersettings";
	private static final String THEME = "theme";
	private static final String STANDARD = "standard";
	private static final String NIGHT = "NIGHT";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setMenuSelected(getIntent().getExtras());
		super.setToolbarTitle(R.string.UserSettingsAcitivity_name);
		super.setShareVisible(false);
		setContentView(R.layout.activity_user_settings);
		
		SeekBar changeRadius;
		Button changeEmailButton;
		Spinner themeSpinner;
		final TextView reportRadiusView;
		String selectedTheme;
		String[] themeSpinnerEntries;
		
		SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		reportRadius = preferences.getInt("ReportRadius", 500);
		seekBarPos = preferences.getInt("SeekBarPos", 4);
		selectedTheme = preferences.getString(THEME, STANDARD);
		Log.i("Settings loaded", selectedTheme);
		
		initialStart = true;
		final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext(), null, null, 1);
		currentEmail = (EditText) findViewById(R.id.UserSettingsActivity_et_currentEmail);
		changeRadius = (SeekBar) findViewById(R.id.UserSettingsActivity_sb_ChangeRadius);
		themeSpinner = (Spinner) findViewById(R.id.UserSettingsActivity_sp_ChangeTheme);
		reportRadiusView = (TextView) findViewById(R.id.UserSettingsActivity_tv_currentRadius);
		changeEmailButton = (Button) findViewById(R.id.UserSettingsActivity_btn_confirmEmail);
		reportRadiusView.setText(reportRadius + " meters");
		toastEmailUpdated = getResources().getString(R.string.incorrect_email);
		
		themeSpinnerEntries = getResources().getStringArray(R.array.themeSpinner);
		
		int selectedThemeIndex = 0;
		if (selectedTheme.equals(STANDARD)) {
			selectedThemeIndex = Arrays.asList(themeSpinnerEntries).indexOf(getResources().getString(R.string.themeStandard));
			Log.i("Selected theme index", "standard: " + selectedThemeIndex);
		} else if (selectedTheme.equals(NIGHT)) {
			selectedThemeIndex = Arrays.asList(themeSpinnerEntries).indexOf(getResources().getString(R.string.themeNight));
			Log.i("Selected theme index", "night: " +selectedThemeIndex);
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_layout_custom_row, themeSpinnerEntries);
		themeSpinner.setAdapter(adapter);
		themeSpinner.setSelection(selectedThemeIndex);
		
		user = dbh.getUser();
		currentEmail.setText(user.getMailAccount());
		
		changeRadius.setProgress(seekBarPos);
		changeRadius.setMax(19);
		
		themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String selected = parent.getItemAtPosition(position).toString();
				
				
				if (!initialStart) {
					Intent i = new Intent(UserSettingsActivity.this, UserSettingsActivity.class);
					startActivity(i);
					finish();
				}
				
				initialStart = false;
				
				if (selected.equals(getResources().getString(R.string.themeStandard))) {
					SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
					editor.putString(THEME, STANDARD);
					editor.commit();
				} else if (selected.equals(getResources().getString(R.string.themeNight))) {
					SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
					editor.putString(THEME, NIGHT);
					editor.commit();
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Log.i(TAG, "onNothingSelected not supported.");
			}
		});
		
		changeEmailButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dbh.updateUser(currentEmail.getText().toString());
				user = dbh.getUser();
				currentEmail.setText(user.getMailAccount());
				
				Toast toast = Toast.makeText(getApplicationContext(), toastEmailUpdated, Toast.LENGTH_SHORT);
				toast.show();
				
			}
		});
		
		
		currentEmail.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				Log.i(TAG, "beforeTextChanged not supported.");
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Log.i(TAG, "onTextChanged not supported.");

			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
					currentEmail.setBackgroundResource(R.drawable.email_border_correct);
				} else {
					currentEmail.setBackgroundResource(R.drawable.email_border_wrong);
				}
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
				Log.i(TAG, "onStartTrackingTouch not supported.");
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Log.i(TAG, "onStopTrackingTouch not supported.");
			}
		});
	}
}
