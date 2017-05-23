package nl.gemeente.breda.bredaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import nl.gemeente.breda.bredaapp.domain.User;

public class UserSettingsActivity extends AppCompatActivity{
	
	private User user;
	private EditText currentEmail;
	private Switch changeSettings;
	private CharSequence currentEmailSequence;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_settings);
		
		final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext(), null, null, 1);
		currentEmail = (EditText) findViewById(R.id.UserSettingsActivity_et_currentEmail);
		changeSettings = (Switch) findViewById(R.id.UserSettingsActivity_sw_ChangeSettings);
		
		currentEmail.setEnabled(false);
		
		user = dbh.getUser();
		currentEmail.setText(user.getMailAccount());
		
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
