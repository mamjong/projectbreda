package nl.gemeente.breda.bredaapp;

import android.os.Bundle;

public class InfoActivity extends AppBaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		super.setMenuSelected(getIntent().getExtras());
		
		super.setToolbarTitle(R.string.info_title);
	}
}
