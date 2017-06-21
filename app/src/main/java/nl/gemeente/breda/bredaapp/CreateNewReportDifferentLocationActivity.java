package nl.gemeente.breda.bredaapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import nl.gemeente.breda.bredaapp.adapter.ServiceAdapter;
import nl.gemeente.breda.bredaapp.businesslogic.ServiceManager;

public class CreateNewReportDifferentLocationActivity extends AppBaseActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {
	
	private static final int GALLERY_PIC_REQUEST = 1338;
	private Button imageButton, continueButton;
	private Bitmap itemImage;
	private ImageView selectedPicture;
	private ServiceAdapter serviceAdapter;
	private String chosenService;
	private EditText commentEditText;
	private TextView noPicture;
	
	private GoogleMap map;
	
	private double latitude;
	private double longitude;
	
	private boolean markerPlaced = false;
	private boolean imageSelected = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setMenuSelected(getIntent().getExtras());
		super.setShareVisible(false);
		setContentView(R.layout.activity_create_new_report_different_location);
		
		imageButton = (Button) findViewById(R.id.activityCreateNewReportDifferentLocation_bt_addImage);
		continueButton = (Button)findViewById(R.id.activityCreateNewReportDifferentLocation_bt_continue);
		selectedPicture = (ImageView)findViewById(R.id.activityCreateNewReportDifferentLocation_iv_defectImage);
		noPicture = (TextView) findViewById(R.id.activityCreateNewReport_tv_noPicture);
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
		
		noPicture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				noPicture();
			}
		});
		
		final Spinner sprCategories = (Spinner) findViewById(R.id.activityCreateNewReport_spr_categories);
		sprCategories.setAdapter(serviceAdapter);
		sprCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				chosenService = sprCategories.getItemAtPosition(position).toString();
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		chosenService = sprCategories.getSelectedItem().toString();
		
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
				
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						try {
							String filename = "inframeld.jpeg";
							
							saveImage(CreateNewReportDifferentLocationActivity.this, itemImage, filename);
							
							String comment = commentEditText.getText().toString();
							
							Intent continueToMapIntent = new Intent(getApplicationContext(), CheckDataActivity.class);
							continueToMapIntent.putExtra("SERVICE", chosenService);
							continueToMapIntent.putExtra("COMMENT", comment);
							continueToMapIntent.putExtra("LATITUDE", latitude);
							continueToMapIntent.putExtra("LONGITUDE", longitude);
							startActivity(continueToMapIntent);
						} catch (RuntimeException e) {
							Toast toastError = Toast.makeText(CreateNewReportDifferentLocationActivity.this, getResources().getString(R.string.activityCreateNewReport_text_imageTooLarge), Toast.LENGTH_LONG);
							toastError.show();
						} catch (Exception e) {
							Log.i("Error", e.getMessage());
						}
					}
				}, 1);
			}
		});
		
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMapView_FL_mapLayout);
		if (mapFragment == null) {
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			mapFragment = SupportMapFragment.newInstance();
			transaction.replace(R.id.fragmentMapView_FL_mapLayout, mapFragment).commit();
		}
		
		if (mapFragment != null) {
			mapFragment.getMapAsync(this);
		}
	}
	
	private void noPicture() {
		itemImage = BitmapFactory.decodeResource(getResources(), R.drawable.nopicturefound);
		selectedPicture.setImageBitmap(itemImage);
		imageSelected = true;
		updateContinueButton();
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
						
						imageSelected = true;
						updateContinueButton();
					} catch (Exception e) {
						Log.i("Error", e.getMessage());
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
			Log.i("Error", e.getMessage());
		} catch (IOException e) {
			Log.i("Error", e.getMessage());
		}
	}
	
	@Override
	public void onMapLongClick(LatLng point) {
		map.clear();
		MarkerOptions markerOptions = new MarkerOptions().position(point).title("").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15.f));
		
		latitude = point.latitude;
		longitude = point.longitude;
		
		map.addMarker(markerOptions);
		markerPlaced = true;
		updateContinueButton();
	}
	
	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		
		LatLngBounds breda = new LatLngBounds(new LatLng(51.482969, 4.654534), new LatLng(51.647188, 4.874748));
		map.setLatLngBoundsForCameraTarget(breda);
		map.setMinZoomPreference(11);
		map.getUiSettings().setMapToolbarEnabled(false);
		map.getUiSettings().setCompassEnabled(false);
		map.getUiSettings().setMyLocationButtonEnabled(false);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(breda.getCenter(), 13.f));
		map.setOnMapLongClickListener(this);
	}
	
	@Override
	public void onMarkerDragStart(Marker marker) {
		
	}
	
	@Override
	public void onMarkerDrag(Marker marker) {
		latitude = marker.getPosition().latitude;
		longitude = marker.getPosition().longitude;
	}
	
	@Override
	public void onMarkerDragEnd(Marker marker) {
		
	}
	
	public void updateContinueButton() {
		if ((imageSelected) && (markerPlaced)) {
			continueButton.setEnabled(true);
		} else {
			continueButton.setEnabled(false);
		}
	}
}
