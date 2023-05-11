package wro.per.activities;

import static java.lang.Math.abs;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.HashMap;

import wro.per.R;
import wro.per.others.LocationService;

public class EditObjectActivity extends AppCompatActivity {

    private Float driftX, driftZ, driftY;
    private Float accelerometerZ;
    private Float rotationZ;
    float tiltInDegrees;
    Bitmap bitmap;
    private EditText tileEditText, detailCoordsEditText, azimuthEditText, coordinates1EditTest, coordinates2EditTest;
    float azimuth, pitch, roll;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    double objectLatitude, objectLongitude, observationLatitude, observationLongitude, latitude, longitude, detailLatitude, detailLongitude;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {
            coordinates1EditTest = findViewById(R.id.wspolrzedne_obserwacji_edittext);
            coordinates2EditTest = findViewById(R.id.wspolrzedne_obiektu_edittext);
            latitude = intent.getDoubleExtra("latitude", 3);
            longitude = intent.getDoubleExtra("longitude", 3);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.object_edit_layout);

        Button takePhotoButton;

        Button sendToDatabaseButton;

        Button getCoordinates1Button;
        Button getCoordinates2Button;

        tileEditText = findViewById(R.id.nachylenieEditText);

        azimuthEditText = findViewById(R.id.kierunekEditText);

        detailCoordsEditText = findViewById(R.id.wspolrzedne_szczegołu_edittext);

        getCoordinates1Button = findViewById(R.id.wspolrzedne_obserwacji_button);
        getCoordinates2Button = findViewById(R.id.wspolrzedne_obiektu_button);

        takePhotoButton = findViewById(R.id.zdjecie_szczegolu_button);

        sendToDatabaseButton = findViewById(R.id.wyslij_button);

        takePhotoButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, CameraActivity.class);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        });


        getCoordinates1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditObjectActivity.this, LocationService.class);
                startService(intent);

                objectLatitude = latitude;
                objectLongitude = longitude;

                coordinates1EditTest.setText(objectLatitude + ",  " + objectLongitude);

            }
        });

        getCoordinates2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditObjectActivity.this, LocationService.class);
                startService(intent);

                observationLatitude = latitude;
                observationLongitude = longitude;

                coordinates2EditTest.setText(observationLatitude + ",  " + observationLongitude);

            }
        });

        sendToDatabaseButton.setOnClickListener(view -> {
            Toast.makeText(this, "Jeszcze nie działa", Toast.LENGTH_LONG).show();
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            String imagePath = data.getStringExtra("imagePath");
            azimuth = data.getFloatExtra("azimuth", 0);
            pitch = data.getFloatExtra("pitch", 0);
            roll = data.getFloatExtra("roll", 0);
            accelerometerZ = data.getFloatExtra("accelerometerZ", 0);
            rotationZ = data.getFloatExtra("rotationZ", 0);
            tiltInDegrees = (float) Math.toDegrees(Math.acos(accelerometerZ / SensorManager.GRAVITY_EARTH));

            tileEditText.setText(Float.toString(tiltInDegrees));
            azimuthEditText.setText(Float.toString(rotationZ));

            detailLatitude = data.getDoubleExtra("lat", 1);
            detailLongitude = data.getDoubleExtra("lon", 1);

            detailCoordsEditText.setText(Double.toString(detailLatitude) + ",  " + Double.toString(detailLongitude));

            bitmap = BitmapFactory.decodeFile(imagePath);
            try {
                ExifInterface exif = new ExifInterface(imagePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                Matrix matrix = new Matrix();
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    matrix.postRotate(90);
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                    matrix.postRotate(180);
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    matrix.postRotate(270);
                }
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView imageView = findViewById(R.id.zdjecie_szczegolu_imageview);
            imageView.setImageBitmap(bitmap);
        }
    }

    private void stopLocationService() {
        Intent intent = new Intent(this, LocationService.class);
        stopService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("ACT_LOC"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationService();
        unregisterReceiver(broadcastReceiver);
    }

    private HashMap<Integer, String> riddlesHashMap = new HashMap<>();


}
