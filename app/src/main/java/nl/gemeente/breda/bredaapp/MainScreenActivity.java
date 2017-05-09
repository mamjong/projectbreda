package nl.gemeente.breda.bredaapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import nl.gemeente.breda.bredaapp.adapter.MainScreenSectionsPagerAdapter;

public class MainScreenActivity extends AppCompatActivity {
	
	//================================================================================
	// Properties
	//================================================================================
	
	private MainScreenSectionsPagerAdapter sectionsPagerAdapter;
	private ViewPager viewPager;
	
	//================================================================================
	// Accessors
	//================================================================================
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		sectionsPagerAdapter = new MainScreenSectionsPagerAdapter(getSupportFragmentManager());
		
		viewPager = (ViewPager) findViewById(R.id.container);
		viewPager.setAdapter(sectionsPagerAdapter);
		
		TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(viewPager);
	}
}
