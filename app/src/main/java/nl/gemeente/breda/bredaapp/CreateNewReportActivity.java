package nl.gemeente.breda.bredaapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import static android.R.attr.data;

public class CreateNewReportActivity extends AppCompatActivity {


    private static final int CAMERA_PIC_REQUEST = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_report);

        Button cameraButton = (Button) findViewById(R.id.activityCreateNewReport_bt_makePicture);

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
