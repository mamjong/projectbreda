package nl.gemeente.breda.bredaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import nl.gemeente.breda.bredaapp.domain.User;

public class UserSettingsActivity extends AppCompatActivity{
	
	private User user;
	private EditText currentEmail;
	private Switch changeSettings;
	private int reportRadius;
	private TextView reportRadiusView;
//	public static final String PREFS_NAME = "PrefsFile";
	private SeekBar changeRadius;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_settings);
		
//		reportRadius = 1000;
//
//		SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
//		editor.putInt("ReportRadius", reportRadius);
//		editor.commit();
		
		final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext(), null, null, 1);
		currentEmail = (EditText) findViewById(R.id.UserSettingsActivity_et_currentEmail);
		changeSettings = (Switch) findViewById(R.id.UserSettingsActivity_sw_ChangeSettings);
		changeRadius = (SeekBar) findViewById(R.id.UserSettingsActivity_sb_ChangeRadius);
		reportRadiusView = (TextView) findViewById(R.id.UserSettingsActivity_tv_currentRadius);
		reportRadiusView.setText("100 meters");
		
		currentEmail.setEnabled(false);
		
		user = dbh.getUser();
		currentEmail.setText(user.getMailAccount());
		
		changeRadius.setProgress(0);
		changeRadius.setMax(19);
		changeRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				reportRadius = progress * 100 + 100;
				reportRadiusView.setText(reportRadius + " meters");
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
		});
		
		changeSettings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked == true) {
					currentEmail.setEnabled(true);
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
					currentEmail.setEnabled(false);
					dbh.updateUser(currentEmail.getText().toString());
					user = dbh.getUser();
					currentEmail.setText(user.getMailAccount());
				}
			}
		});
	}
}
