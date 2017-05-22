package nl.gemeente.breda.bredaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import nl.gemeente.breda.bredaapp.domain.User;

public class UserSettingsActivity extends AppCompatActivity{
	
	private User user;
	private EditText currentEmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_settings);
		
		DatabaseHandler dbh = new DatabaseHandler(getApplicationContext(), null, null, 1);
		
		currentEmail = (EditText) findViewById(R.id.UserSettingsActivity_et_currentEmail);
		user = dbh.getUser();
		
		currentEmail.setText(user.getMailAccount());
	}
}
