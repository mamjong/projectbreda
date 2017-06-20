package nl.gemeente.breda.bredaapp.eastereggs;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.VideoView;

import java.util.Objects;

import nl.gemeente.breda.bredaapp.R;

public class EasterEgg extends AppCompatActivity {
	
	private VideoView videoView;
	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.i("TYPE", String.valueOf(getIntent().getSerializableExtra("TYPE")));
		
		if(Objects.equals(String.valueOf(getIntent().getSerializableExtra("TYPE")), "VIDEO")) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.activity_video_easter_egg);
			videoView = (VideoView) findViewById(R.id.videoEasteregg);
			Uri path = Uri.parse("android.resource://" + getPackageName() + "/raw/" + getIntent().getSerializableExtra("VIDEONAME"));
			videoView.setVideoURI(path);
			videoView.requestFocus();
			videoView.start();
		} else if (Objects.equals(String.valueOf(getIntent().getSerializableExtra("TYPE")), "WEBSITE")) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			setContentView(R.layout.activity_website_easter_egg);
			webView = (WebView) findViewById(R.id.websiteEasteregg);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.loadUrl((String) getIntent().getSerializableExtra("URL"));
		}
	}
}
