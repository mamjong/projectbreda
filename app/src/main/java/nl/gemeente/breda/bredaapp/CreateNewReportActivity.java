package nl.gemeente.breda.bredaapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import nl.gemeente.breda.bredaapp.adapter.ServiceAdapter;
import nl.gemeente.breda.bredaapp.api.LocationApi;
import nl.gemeente.breda.bredaapp.businesslogic.ServiceManager;
import nl.gemeente.breda.bredaapp.util.AlertCreator;


public class CreateNewReportActivity extends AppBaseActivity implements LocationApi.LocationListener {
	
	private static final int CAMERA_PIC_REQUEST = 1337;
	private static final int GALLERY_PIC_REQUEST = 1338;
	private String[] arraySpinnerGroenSubs, arraySpinnerAfvalSubs, arraySpinnerDierenEnOngedierteSubs, arraySpinnerOpenbareVerlichtingSubs;
	private ServiceAdapter serviceAdapter;
	private String chosenService;
	private Bitmap itemImage;
	private Button cameraButton;
	private Button continueToMap;
	private TextView noPicture;
	private ImageView selectedPictureView;
	private EditText commentText;
	
	private LocationApi locationService;
	private double locationLatitude;
	private double locationLongitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_report);
		super.setMenuSelected(getIntent().getExtras());
		super.setShareVisible(false);
		
		cameraButton = (Button) findViewById(R.id.activityCreateNewReport_bt_makePicture);
		continueToMap = (Button) findViewById(R.id.activityCreateNewReport_bt_continue);
		noPicture = (TextView) findViewById(R.id.activityCreateNewReport_tv_noPicture);
		selectedPictureView = (ImageView) findViewById(R.id.activityCreateNewReport_iv_defectImage);
		commentText = (EditText) findViewById(R.id.activityCreateNewReport_et_commentText);
		serviceAdapter = new ServiceAdapter(getApplicationContext(), ServiceManager.getServices(), R.layout.spinner_layout_custom_row);
		
		continueToMap.setEnabled(false);
		
		locationService = new LocationApi(this);
		getLocation();
		
		noPicture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				noPicture();
			}
		});
		
		
		// Service spinner -- Wordt opgehaald van de API
		Spinner sprCategories = (Spinner) findViewById(R.id.activityCreateNewReport_spr_categories);
		
		sprCategories.setAdapter(serviceAdapter);
		
		continueToMap.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (itemImage == null) {
					Toast toast = Toast.makeText(CreateNewReportActivity.this, getResources().getString(R.string.activityCreateNewReport_text_noImageSelected), Toast.LENGTH_LONG);
					toast.show();
					return;
				}
				Log.i("Create report", "Next clicked");
				continueToMap.setEnabled(false);
				continueToMap.setText(getResources().getString(R.string.spinner_loading));
				
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						try {
							String filename = "inframeld.jpeg";
							
							saveImage(CreateNewReportActivity.this, itemImage, filename);
							commentText.getText();
							
							Intent continueToMapIntent = new Intent(getApplicationContext(), CheckDataActivity.class);
							continueToMapIntent.putExtra("SERVICE", chosenService);
							
							startActivity(continueToMapIntent);
						} catch (RuntimeException e) {
							Toast toastError = Toast.makeText(CreateNewReportActivity.this, getResources().getString(R.string.activityCreateNewReport_text_imageTooLarge), Toast.LENGTH_LONG);
							toastError.show();
						} catch (Exception e) {
							Log.e("ERR", String.valueOf(e));
						}
					}
				}, 1);
			}
		});
		
		cameraButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertCreator popup = new AlertCreator(CreateNewReportActivity.this);
				popup.setTitle(getResources().getString(R.string.activityCreateNewReport_text_chooseSource));
				popup.setMessage(getResources().getString(R.string.activityCreateNewReport_text_chooseSourceText));
				popup.setIcon(R.mipmap.ic_launcher);
				popup.setPositiveButton(getResources().getString(R.string.activityCreateNewReport_text_itemCamera), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
					}
				});
				popup.setNegativeButton(getResources().getString(R.string.activityCreateNewReport_text_itemGallery), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
						galleryIntent.setType("image/*");
						galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(galleryIntent, GALLERY_PIC_REQUEST);
					}
				});
				popup.show();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (selectedPictureView.getDrawable() != null) {
			continueToMap.setEnabled(true);
			continueToMap.setText(getResources().getString(R.string.activityCreateNewReport_bt_continue));
		}
		
		getLocation();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	private void noPicture() {
		itemImage = BitmapFactory.decodeResource(getResources(), R.drawable.nopicturefound);
		selectedPictureView.setImageBitmap(itemImage);
		continueToMap.setEnabled(true);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case CAMERA_PIC_REQUEST:
					Bitmap defectImage = (Bitmap) data.getExtras().get("data");
					this.itemImage = defectImage;
					selectedPictureView.setImageBitmap(defectImage);
					continueToMap.setEnabled(true);
					break;
				
				case GALLERY_PIC_REQUEST:
					Uri selectedImage = data.getData();
					try {
						Bitmap picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
						this.itemImage = picture;
						selectedPictureView.setImageBitmap(picture);
						continueToMap.setEnabled(true);
					} catch (Exception e) {
						Log.e("ERR", String.valueOf(e));
					}
					break;
				
				default:
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
			Log.e("ERR", String.valueOf(e));
		} catch (IOException e) {
			Log.e("ERR", String.valueOf(e));
		}
	}
	
	public void getLocation() {
		locationService.setContext(getApplicationContext(), this);
		locationService.search();
	}
	
	public void noLocation() {
		Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(i);
		Toast.makeText(this, getString(R.string.location_off_text), Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onLocationAvailable(double latitude, double longtitude) {
		locationLatitude = latitude;
		locationLongitude = longtitude;
	}
	
	@Override
	public void onLocationError() {
		noLocation();
	}
}




