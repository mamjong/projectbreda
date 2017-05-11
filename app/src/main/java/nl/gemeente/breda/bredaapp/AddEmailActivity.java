package nl.gemeente.breda.bredaapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class AddEmailActivity extends AppCompatActivity {
	
	private Button emailConfirmBtn;
	private EditText emailInputBox;
	private String email;
	private DatabaseHandler dbh;
	private CheckBox readTOSCheck;
	private TextView terms;
	
	private final int ACCEPT_TERMS = 999;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_email);
		
		dbh = new DatabaseHandler(this, null, null, 1);
		
		emailInputBox = (EditText) findViewById(R.id.AddEmailActivity_et_emailInput);
		emailConfirmBtn = (Button) findViewById(R.id.AddEmailActivity_bt_emailConfirmButton);
		
		emailConfirmBtn.setEnabled(false);
		
		emailInputBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.toString().trim().length() == 0) {
					emailConfirmBtn.setEnabled(false);
				} else {
					emailConfirmBtn.setEnabled(true);
				}
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		readTOSCheck = (CheckBox) findViewById(R.id.AddEmailActivity_cb_termsAccepted);
		readTOSCheck.setEnabled(false);
		
		terms = (TextView) findViewById(R.id.AddEmailActivity_tv_TermsText);
		terms.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), TermsAndConditionsActivity.class);
				startActivityForResult(i, ACCEPT_TERMS);
			}
		});
		
		emailConfirmBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
				email = emailInputBox.getText().toString();
				dbh.addUser(email);
				startActivity(i);
				finish();
			}
		});
	}
	
	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACCEPT_TERMS) {
			if (resultCode == RESULT_OK) {
				boolean accepted = data.getBooleanExtra("accepted", false);
				readTOSCheck.setChecked(accepted);
				readTOSCheck.setEnabled(true);
			}
		}
	}
}
		

