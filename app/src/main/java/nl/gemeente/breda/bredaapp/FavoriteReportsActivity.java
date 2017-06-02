package nl.gemeente.breda.bredaapp;

import android.os.Bundle;
import android.provider.ContactsContract;
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
	private DatabaseHandler dbh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_reports);
		super.setMenuSelected(getIntent().getExtras());
		
		final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext(), null, null, 1);
		
		favoriteReportsListView = (ListView) findViewById(R.id.favoritescreen_lv);
		reports = dbh.getAllReports();
		favoriteReportsAdapter = new FavoriteReportsAdapter(getApplicationContext(), reports);
		
		favoriteReportsListView.setAdapter(favoriteReportsAdapter);
		
		
	}
}
