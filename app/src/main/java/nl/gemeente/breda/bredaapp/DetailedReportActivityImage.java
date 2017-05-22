package nl.gemeente.breda.bredaapp;

import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoViewAttacher;

import nl.gemeente.breda.bredaapp.api.ImageLoader;
import nl.gemeente.breda.bredaapp.domain.Report;

import static android.R.attr.spacing;
import static nl.gemeente.breda.bredaapp.fragment.MainScreenListFragment.EXTRA_REPORT;

public class DetailedReportActivityImage extends AppCompatActivity{
	private ImageView mediaUrl;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailed_report_fullscreen_image);
		
		//remove actionbar & statusbar when in landscape mode
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
															|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
															|View.SYSTEM_UI_FLAG_FULLSCREEN
															|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
															|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
															|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
		} else {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	
		mediaUrl = (ImageView) findViewById(R.id.DetailedReportActivityFullscreenImage_IV_Image);
		PhotoViewAttacher photoView = new PhotoViewAttacher(mediaUrl);
		photoView.update();
		
		Bundle extras = getIntent().getExtras();
		final Report r = (Report) extras.getSerializable(EXTRA_REPORT);
		
		new ImageLoader(mediaUrl).execute(r.getMediaUrl());
	}
}