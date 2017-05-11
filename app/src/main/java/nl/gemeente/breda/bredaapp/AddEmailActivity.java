package nl.gemeente.breda.bredaapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddEmailActivity extends AppCompatActivity {
	
	private Button emailConfirmBtn;
	private EditText emailInputBox;
	private String email;
	private DatabaseHandler dbh;
	private CheckBox readTOSCheck;
	private TextView terms;
	private boolean mailOkay;
	private boolean termsOkay;
	
	private final int ACCEPT_TERMS = 999;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_email);
		
		dbh = new DatabaseHandler(this, null, null, 1);
		
		emailInputBox = (EditText) findViewById(R.id.AddEmailActivity_et_emailInput);
		emailConfirmBtn = (Button) findViewById(R.id.AddEmailActivity_bt_emailConfirmButton);
		
		mailOkay = false;
		termsOkay = false;
		
		emailInputBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				if (s.toString().trim().length() == 0) {
//					//emailConfirmBtn.setEnabled(false);
//					mailOkay = false;
//				} else if (isValidEmail(s)){
//					//emailConfirmBtn.setEnabled(true);
//					mailOkay = true;
//				}
				checkMail(s);
				submitButtonState();
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		readTOSCheck = (CheckBox) findViewById(R.id.AddEmailActivity_cb_termsAccepted);
		readTOSCheck.setEnabled(false);
		readTOSCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				termsOkay = readTOSCheck.isChecked();
				submitButtonState();
			}
		});
		
		terms = (TextView) findViewById(R.id.AddEmailActivity_tv_TermsText);
		terms.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				emailConfirmBtn.setEnabled(false);
				Intent i = new Intent(getApplicationContext(), TermsAndConditionsActivity.class);
				startActivityForResult(i, ACCEPT_TERMS);
			}
		});
		
		emailConfirmBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("Button Submit", "Submitted");
				if (canButtonSubmit()) {
					Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
					email = emailInputBox.getText().toString();
					dbh.addUser(email);
					startActivity(i);
					finish();
				} else {
					Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.activityEmail_no_submit), Toast.LENGTH_SHORT);
					toast.show();
				}
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
				emailConfirmBtn.setEnabled(true);
				boolean accepted = data.getBooleanExtra("accepted", false);
				readTOSCheck.setChecked(accepted);
				readTOSCheck.setEnabled(true);
				termsOkay = true;
				checkMail();
			}
		}
	}
	
	public void submitButtonState() {
		if (termsOkay && mailOkay) {
			emailConfirmBtn.setEnabled(true);
		} else {
			emailConfirmBtn.setEnabled(false);
		}
	}
	
	public boolean canButtonSubmit() {
		return (termsOkay && mailOkay);
	}
	
	public boolean checkMail(CharSequence s) {
		if (s.toString().trim().length() == 0) {
			mailOkay = false;
			return false;
		}
		
		if (!isValidEmail(s)) {
			mailOkay = false;
			return false;
		}
		
		mailOkay = true;
		Log.i("Mail Okay:", mailOkay + "");
		Log.i("Terms Okay:", termsOkay + "");
		Log.i("Can submit:", canButtonSubmit() + "");
		return true;
	}
	
	public boolean checkMail() {
		CharSequence s = emailInputBox.getText();
		Log.i("Test", s.toString());
		return checkMail(s);
	}
}
		

