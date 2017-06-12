package nl.gemeente.breda.bredaapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.android.gms.maps.GoogleMap;

import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.fragment.MainScreenListFragment;
import nl.gemeente.breda.bredaapp.fragment.MainScreenMapFragment;

public class MainScreenSectionsPagerAdapter extends FragmentPagerAdapter {
	
	private Context context;
	private MainScreenMapFragment tab1;
	private MainScreenListFragment tab2;
	//================================================================================
	// Constructors
	//================================================================================
	
	public MainScreenSectionsPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				tab1 = new MainScreenMapFragment();
				return tab1;
			
			case 1:
				tab2 = new MainScreenListFragment();
				return tab2;
			
			default:
				return null;
		}
	}
	
	public Fragment getTab1() {
		return tab1;
	}
	
	public MainScreenMapFragment getMapFragment() {
		return tab1;
	}
	
	public MainScreenListFragment getListFragment() {
		return tab2;
	}
	
	public Fragment getTab2() {
		return tab2;
	}
	
	@Override
	public int getCount() {
		return 2;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
			case 0:
				return context.getResources().getString(R.string.homescreen_left_tab);
			case 1:
				return context.getResources().getString(R.string.homescreen_right_tab);
			default:
				break;
		}
		return null;
	}
	
	public void removeMarkers() {
		
		if (tab1 != null) {
			tab1.removeMarkers();
		}
	}
	
	public GoogleMap getMap() {
		if (tab1 != null) {
			return tab1.getMap();
		}
		return null;
	}
}