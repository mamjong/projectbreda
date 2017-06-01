package nl.gemeente.breda.bredaapp;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import nl.gemeente.breda.bredaapp.adapter.FavoriteReportsAdapter;
import nl.gemeente.breda.bredaapp.adapter.ReportAdapter;
import nl.gemeente.breda.bredaapp.businesslogic.ReportManager;
import nl.gemeente.breda.bredaapp.domain.Report;

public class FavoriteReportsActivity extends AppBaseActivity {
	
	private ListView favoriteReportsListView;
	private FavoriteReportsAdapter favoriteReportsAdapter;
	private ArrayList<Report> reports = new ArrayList<Report>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_reports);
		super.setMenuSelected(getIntent().getExtras());
		
		favoriteReportsListView = (ListView) findViewById(R.id.favoritescreen_lv);
		//favoriteReportsAdapter = new FavoriteReportsAdapter();
		favoriteReportsListView.setAdapter(favoriteReportsAdapter);
		
	}
}
