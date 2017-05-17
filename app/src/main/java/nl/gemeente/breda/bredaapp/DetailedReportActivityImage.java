package nl.gemeente.breda.bredaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import nl.gemeente.breda.bredaapp.api.ImageLoader;
import nl.gemeente.breda.bredaapp.domain.Report;

import static nl.gemeente.breda.bredaapp.fragment.MainScreenListFragment.EXTRA_REPORT;

public class DetailedReportActivityImage extends AppCompatActivity {
	private ImageView mediaUrl;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailed_report_fullscreen_image);
		
	
		mediaUrl = (ImageView) findViewById(R.id.DetailedReportActivityFullscreenImage_IV_Image);
		
		Bundle extras = getIntent().getExtras();
		final Report r = (Report) extras.getSerializable(EXTRA_REPORT);
		
		new ImageLoader(mediaUrl).execute(r.getMediaUrl());
	}
}
