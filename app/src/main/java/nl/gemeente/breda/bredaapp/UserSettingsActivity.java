package nl.gemeente.breda.bredaapp;

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
	private Button changeInfo;
	private EditText currentEmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_settings);
		
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext(), null, null, 1);
		
		currentEmail = (EditText) findViewById(R.id.UserSettingsActivity_et_currentEmail);
		currentEmail.setEnabled(false);
		
		user = dbh.getUser();
		currentEmail.setText(user.getMailAccount());
		
		changeInfo = (Button) findViewById(R.id.UserSettingsActivity_bt_changeInfoBtn);
		changeInfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				changeInfo.setVisibility(View.GONE);
				currentEmail.setEnabled(true);
			}
		});
		
		currentEmail.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.toString().trim().length() == 0) {
					//button.setEnabled(false);
				} else if (Patterns.EMAIL_ADDRESS.matcher(s).matches()){
					//button.setEnabled(true);
				}
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
	}
}
