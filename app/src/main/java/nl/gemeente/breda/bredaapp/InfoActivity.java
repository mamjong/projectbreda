package nl.gemeente.breda.bredaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InfoActivity extends AppBaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		super.setMenuSelected(getIntent().getExtras());
		
		
	}
}
