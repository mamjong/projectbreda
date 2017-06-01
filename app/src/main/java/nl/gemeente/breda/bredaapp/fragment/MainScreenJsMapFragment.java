package nl.gemeente.breda.bredaapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.util.ArrayList;

import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.domain.Report;

public class MainScreenJsMapFragment extends Fragment {
	
	private ArrayList<Report> reports;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_js_map_view, container, false);
		
		reports = new ArrayList<>();
		
		WebView mapWebview = (WebView) rootView.findViewById(R.id.map_webview);
		mapWebview.getSettings().setJavaScriptEnabled(true);
		mapWebview.getSettings().setDomStorageEnabled(true);
		mapWebview.loadUrl("http://bredaapi.thomasmout.nl/");
		
		return rootView;
	}
}
