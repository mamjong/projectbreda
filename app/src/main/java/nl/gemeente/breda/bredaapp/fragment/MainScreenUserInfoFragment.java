package nl.gemeente.breda.bredaapp.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import nl.gemeente.breda.bredaapp.MainScreenActivity;
import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.adapter.ReportAdapter;

public class MainScreenUserInfoFragment extends Fragment {
	
	//================================================================================
	// Properties
	//================================================================================
	
	private ImageView overlay;
	private TextView overlayNew;
	//================================================================================
	// Accessors
	//================================================================================
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.activity_main_screen_user_info_fragment, container, false);
		
			//overlay = (ImageView) rootView.findViewById(R.id.activityMainscreen_overlay_image);
			//overlayNew = (TextView) rootView.findViewById(R.id.activityMainscreen_overlay_imageNew);
		
		return rootView;
	}
	
	public void showOverlay() {
		MainScreenActivity.overlay.setVisibility(View.VISIBLE);
	}
}




