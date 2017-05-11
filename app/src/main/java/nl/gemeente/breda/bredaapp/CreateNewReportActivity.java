package nl.gemeente.breda.bredaapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import nl.gemeente.breda.bredaapp.adapter.ServiceAdapter;
import nl.gemeente.breda.bredaapp.businesslogic.ServiceManager;


public class CreateNewReportActivity extends AppCompatActivity {
	
	
	private static final int CAMERA_PIC_REQUEST = 1337; // LEET
	private String[] arraySpinnerDataMain, arraySpinnerGroenSubs, arraySpinnerAfvalSubs, arraySpinnerDierenEnOngedierteSubs, arraySpinnerOpenbareVerlichtingSubs;
	private ServiceAdapter serviceAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_report);
		
		Button cameraButton = (Button) findViewById(R.id.activityCreateNewReport_bt_makePicture);
		Button continueToMap = (Button) findViewById(R.id.activityCreateNewReport_bt_continue);
		
		serviceAdapter = new ServiceAdapter(getApplicationContext(), ServiceManager.getServices(), R.layout.spinner_layout_custom_row);
		
		
		// TODO: Make 2nd map screen to choose object.
//        continueToMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), )
//            }
//        });
		
		
		// Placeholder spinner data
		this.arraySpinnerDataMain = getResources().getStringArray(R.array.spinnerPlaceHolderData);
		this.arraySpinnerAfvalSubs = getResources().getStringArray(R.array.spinnerAfvalSubs);
		this.arraySpinnerDierenEnOngedierteSubs = getResources().getStringArray(R.array.spinnerDierenEnOngedierteSubs);
		this.arraySpinnerGroenSubs = getResources().getStringArray(R.array.spinnerGroenSubs);
		this.arraySpinnerOpenbareVerlichtingSubs = getResources().getStringArray(R.array.spinnerOpenbareVerlichtingSubs);
		
		
		final Spinner sprSubCategories = (Spinner) findViewById(R.id.activityCreateNewReport_spr_defects);
		Spinner sprCategories = (Spinner) findViewById(R.id.activityCreateNewReport_spr_categories);
		
		
//		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_spinner_item, arraySpinnerDataMain);
		
		
		
		
		sprCategories.setAdapter(serviceAdapter);
		
		
		// Categorie -- TODO: Ophalen van de API
		sprCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                Toast.makeText(parent.getContext(),
//                        "OnItemSelectedListener: " + parent.getItemAtPosition(pos).toString(),
//                        Toast.LENGTH_SHORT).show();
				
				switch (parent.getItemAtPosition(pos).toString()) {
					case "Groen":
						ArrayAdapter<String> sprSubGroenAdapter = new ArrayAdapter<String>(CreateNewReportActivity.this,
								android.R.layout.simple_spinner_item, arraySpinnerGroenSubs);
						sprSubCategories.setAdapter(sprSubGroenAdapter);
						break;
					case "Openbare verlichting":
						ArrayAdapter<String> sprSubOVAdapter = new ArrayAdapter<String>(CreateNewReportActivity.this,
								android.R.layout.simple_spinner_item, arraySpinnerOpenbareVerlichtingSubs);
						sprSubCategories.setAdapter(sprSubOVAdapter);
						break;
					case "Afval":
						ArrayAdapter<String> sprSubAfvalAdapter = new ArrayAdapter<String>(CreateNewReportActivity.this,
								android.R.layout.simple_spinner_item, arraySpinnerAfvalSubs);
						sprSubCategories.setAdapter(sprSubAfvalAdapter);
						break;
					case "Dieren en ongedierte":
						ArrayAdapter<String> sprSubDierAdapter = new ArrayAdapter<String>(CreateNewReportActivity.this,
								android.R.layout.simple_spinner_item, arraySpinnerDierenEnOngedierteSubs);
						sprSubCategories.setAdapter(sprSubDierAdapter);
						break;
				}
				
				
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Toast.makeText(parent.getContext(), "Nothing selected", Toast.LENGTH_LONG).show();
			}
			
		});
		
		
		cameraButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
			}
		});
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_PIC_REQUEST) {
			Bitmap defectImage = (Bitmap) data.getExtras().get("data");
			ImageView imageview = (ImageView) findViewById(R.id.activityCreateNewReport_iv_defectImage);
			imageview.setImageBitmap(defectImage);
		}
	}
}




