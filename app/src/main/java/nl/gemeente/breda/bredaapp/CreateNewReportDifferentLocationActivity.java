package nl.gemeente.breda.bredaapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import nl.gemeente.breda.bredaapp.adapter.ServiceAdapter;
import nl.gemeente.breda.bredaapp.businesslogic.ServiceManager;

public class CreateNewReportDifferentLocationActivity extends AppBaseActivity {
	
	private static final int GALLERY_PIC_REQUEST = 1338;
	private Button imageButton, continueButton;
	private Bitmap itemImage;
	private ImageView selectedPicture;
	private ServiceAdapter serviceAdapter;
	private String chosenService;
	private String[] arraySpinnerDataMain, arraySpinnerGroenSubs, arraySpinnerAfvalSubs, arraySpinnerDierenEnOngedierteSubs, arraySpinnerOpenbareVerlichtingSubs;
	private EditText commentEditText;
	private String commentText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setMenuSelected(getIntent().getExtras());
		setContentView(R.layout.activity_create_new_report_different_location);
		
		imageButton = (Button) findViewById(R.id.activityCreateNewReportDifferentLocation_bt_addImage);
		continueButton = (Button)findViewById(R.id.activityCreateNewReportDifferentLocation_bt_continue);
		selectedPicture = (ImageView)findViewById(R.id.activityCreateNewReportDifferentLocation_iv_defectImage);
		serviceAdapter = new ServiceAdapter(getApplicationContext(), ServiceManager.getServices(), R.layout.spinner_layout_custom_row);
		commentEditText = (EditText)findViewById(R.id.activityCreateNewReportDifferentLocation_et_commentText);
		
		continueButton.setEnabled(false);
		
		imageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				galleryIntent.setType("image/*");
				galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(galleryIntent, GALLERY_PIC_REQUEST);
			}
		});
		
		commentText = commentEditText.toString();
		
		// Placeholder spinner data
//		this.arraySpinnerDataMain = getResources().getStringArray(R.array.spinnerPlaceHolderData);
//		this.arraySpinnerAfvalSubs = getResources().getStringArray(R.array.spinnerAfvalSubs);
//		this.arraySpinnerDierenEnOngedierteSubs = getResources().getStringArray(R.array.spinnerDierenEnOngedierteSubs);
//		this.arraySpinnerGroenSubs = getResources().getStringArray(R.array.spinnerGroenSubs);
//		this.arraySpinnerOpenbareVerlichtingSubs = getResources().getStringArray(R.array.spinnerOpenbareVerlichtingSubs);
//
//		// Service spinner -- Wordt opgehaald van de API
//		final Spinner sprSubCategories = (Spinner) findViewById(R.id.activityCreateNewReport_spr_defects);
//		Spinner sprCategories = (Spinner) findViewById(R.id.activityCreateNewReport_spr_categories);
//
////		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
////				android.R.layout.simple_spinner_item, arraySpinnerDataMain);
//
//		sprCategories.setAdapter(serviceAdapter);
//
//		// Subcategories: TODO: Maken aan de hand van de API
//		sprCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
////                Toast.makeText(parent.getContext(),
////                        "OnItemSelectedListener: " + parent.getItemAtPosition(pos).toString(),
////                        Toast.LENGTH_SHORT).show();
//
//				chosenService = parent.getItemAtPosition(pos).toString();
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				Toast.makeText(parent.getContext(), "Nothing selected", Toast.LENGTH_LONG).show();
//			}
//		});
		
		continueButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if (itemImage == null) {
					Toast toast = Toast.makeText(CreateNewReportDifferentLocationActivity.this, getResources().getString(R.string.activityCreateNewReport_text_noImageSelected), Toast.LENGTH_LONG);
					toast.show();
					return;
				}
				Log.i("Create report", "Next clicked");
				continueButton.setEnabled(false);
				continueButton.setText(getResources().getString(R.string.spinner_loading));
				//continueToMap.setBackgroundResource(R.color.colorPrimaryLight);
				
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						try {
							String filename = "inframeld.jpeg";
							
							saveImage(CreateNewReportDifferentLocationActivity.this, itemImage, filename);
							
							Intent continueToMapIntent = new Intent(getApplicationContext(), CheckDataActivity.class);
							continueToMapIntent.putExtra("SERVICE", chosenService);
							
							startActivity(continueToMapIntent);
						} catch (RuntimeException e) {
							Toast toastError = Toast.makeText(CreateNewReportDifferentLocationActivity.this, getResources().getString(R.string.activityCreateNewReport_text_imageTooLarge), Toast.LENGTH_LONG);
							toastError.show();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, 1);
			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case GALLERY_PIC_REQUEST:
					Uri selectedImage = data.getData();
					try {
						Bitmap picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
						this.itemImage = picture;
						selectedPicture.setImageBitmap(picture);
						continueButton.setEnabled(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
			}
		}
	}
	
	private void saveImage(Context context, Bitmap bitmap, String name) {
		FileOutputStream fos;
		
		try {
			fos = context.openFileOutput(name, Context.MODE_PRIVATE);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
