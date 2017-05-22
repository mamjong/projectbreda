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
		
		
		
//		myDatabaseHelper = new DatabaseHelper();
//		myDatabaseHelper.openDataBase();
//
//		String text = myDatabaseHelper.getYourData(); //this is the method to query
		currentEmail = (EditText) findViewById(R.id.UserSettingsActivity_et_currentEmail);
	
		currentEmail.setText(user.getMailAccount());
//		myDatabaseHelper.close();
//		// set text to your TextView
		
		
		
		
		
		
	}
}
