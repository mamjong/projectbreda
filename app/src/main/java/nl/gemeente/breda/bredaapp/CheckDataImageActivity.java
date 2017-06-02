package nl.gemeente.breda.bredaapp;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoViewAttacher;

public class CheckDataImageActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_data_image);
		
		//remove actionbar & statusbar when in landscape mode
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
		} else {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		
		ImageView itemImageView = (ImageView) findViewById(R.id.CheckDataImageActivity_iv_defectImage);
		PhotoViewAttacher photoView = new PhotoViewAttacher(itemImageView);
		photoView.update();
		
		Bitmap bitmap = CheckDataActivity.loadBitmap(CheckDataImageActivity.this, "inframeld.jpeg");
		itemImageView.setImageBitmap(bitmap);
	}
}
