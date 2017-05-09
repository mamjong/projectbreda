package nl.gemeente.breda.bredaapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nl.gemeente.breda.bredaapp.fragment.MainScreenListFragment;
import nl.gemeente.breda.bredaapp.fragment.MainScreenMapFragment;

public class MainScreenSectionsPagerAdapter extends FragmentPagerAdapter {
	
	//================================================================================
	// Constructors
	//================================================================================
	
	public MainScreenSectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				MainScreenMapFragment tab1 = new MainScreenMapFragment();
				return tab1;
			
			case 1:
				MainScreenListFragment tab2 = new MainScreenListFragment();
				return tab2;
			
			default:
				return null;
		}
	}
	
	@Override
	public int getCount() {
		return 2;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
			case 0:
				return "Map";
			case 1:
				return "Overzicht";
		}
		return null;
	}
}