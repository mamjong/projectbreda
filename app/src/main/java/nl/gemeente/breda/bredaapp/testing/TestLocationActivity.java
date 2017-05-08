package nl.gemeente.breda.bredaapp.testing;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.util.LocationTracker;

public class TestLocationActivity extends AppCompatActivity {
	
	private Button submitButton;
	private LocationTracker tracker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_location);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		submitButton = (Button) findViewById(R.id.testLocation_btn_submit);
		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				tracker = new LocationTracker(TestLocationActivity.this);
				
				if (tracker.canGetLocation()) {
					double latitude = tracker.getLatitude();
					double longitude = tracker.getLongitude();
					
					Toast.makeText(getApplicationContext(), "Your location is - \nLat:" + latitude + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();
				} else {
					tracker.showNoLocationAlert();
				}
			}
		});
	}
}
