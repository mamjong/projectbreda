package nl.gemeente.breda.bredaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import nl.gemeente.breda.bredaapp.domain.User;

public class UserSettingsActivity extends AppCompatActivity{
	
	private User user;
	private Button changeEmail, applyNewEmail;
	private EditText currentEmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_settings);
		
		final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext(), null, null, 1);
		
		currentEmail = (EditText) findViewById(R.id.UserSettingsActivity_et_currentEmail);
		
		currentEmail.setEnabled(false);
		
		user = dbh.getUser();
		currentEmail.setText(user.getMailAccount());
		
		changeEmail = (Button) findViewById(R.id.UserSettingsActivity_bt_changeInfoBtn);
		applyNewEmail = (Button) findViewById(R.id.UserSettingsActivity_bt_applyInfoBtn);
		applyNewEmail.setVisibility(View.GONE);
		
		changeEmail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentEmail.setEnabled(true);
				applyNewEmail.setVisibility(View.VISIBLE);
				changeEmail.setVisibility(View.GONE);
			}
		});
		
		applyNewEmail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CharSequence s = currentEmail.getText();
				String email = currentEmail.toString();
				if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
					dbh.updateUser(email);
				} else {
					currentEmail.setText("WRONG");
				}
			}
		});
	}
}
