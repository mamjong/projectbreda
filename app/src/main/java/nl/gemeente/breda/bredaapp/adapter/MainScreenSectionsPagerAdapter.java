package nl.gemeente.breda.bredaapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import nl.gemeente.breda.bredaapp.MainScreenActivity;
import nl.gemeente.breda.bredaapp.businesslogic.ReportManager;
import nl.gemeente.breda.bredaapp.fragment.MainScreenListFragment;
import nl.gemeente.breda.bredaapp.fragment.MainScreenMapFragment;

import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.fragment.MainScreenUserInfoFragment;



import nl.gemeente.breda.bredaapp.fragment.MainScreenListFragment;
import nl.gemeente.breda.bredaapp.fragment.MainScreenMapFragment;


public class MainScreenSectionsPagerAdapter extends FragmentPagerAdapter {

	private Context context;
	private MainScreenMapFragment tab1;
	private MainScreenListFragment tab2;
	private MainScreenUserInfoFragment tab3;

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
			
			case 2:
				tab3 = new MainScreenUserInfoFragment();
				return tab3;
			
			default:
				return null;
		}
	}

	public Fragment getTab1(){
		return tab1;
	}

	public Fragment getTab2(){
		return tab2;
	}
	
	public Fragment getTab3(){
		return tab3;
	}

	@Override
	public int getCount() {
		return 3;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
			case 0:
				return context.getResources().getString(R.string.homescreen_left_tab);
			case 1:
				return context.getResources().getString(R.string.homescreen_right_tab);
			case 2:
				return context.getResources().getString(R.string.homescreen_right2_tab);
		}
		return null;
	}

	public void removeMarkers(){
		tab1.removeMarkers();
	}
	
	public void showOverlay() {
		
		tab1.showOverlay();
		tab2.dontShowOverlay();
		tab3.showOverlay();
	}
	
}