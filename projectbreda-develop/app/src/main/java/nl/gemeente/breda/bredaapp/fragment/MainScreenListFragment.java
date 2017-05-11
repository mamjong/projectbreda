package nl.gemeente.breda.bredaapp.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.gemeente.breda.bredaapp.R;

public class MainScreenListFragment extends Fragment {
	
	//================================================================================
	// Properties
	//================================================================================
	
	//================================================================================
	// Accessors
	//================================================================================
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_main_screen, container, false);
		
		return rootView;
	}
}
