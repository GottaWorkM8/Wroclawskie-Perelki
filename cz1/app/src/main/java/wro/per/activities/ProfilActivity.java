package wro.per.activities;

import static java.lang.Math.abs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import wro.per.R;

public class ProfilActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor accelerometr;

    private float x, y, z;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 11;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edycja_obiektu_layout);

        Button wykonajZdjecieButton;
        wykonajZdjecieButton = findViewById(R.id.zdjecie_szczegolu_button);
        wykonajZdjecieButton.setOnClickListener(view -> takePicture());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometr = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Bundle extras = result.getData().getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");

                    ImageView imageView = findViewById(R.id.zdjecie_szczegolu_imageview);
                    imageView.setImageBitmap(imageBitmap);

                    if(abs(x) < 1)
                    {
                        System.out.println("X wynosi: "+x);
                        //jest git
                        Toast.makeText(this, "Jest prosto", Toast.LENGTH_SHORT).show();
                    }
                    // tu należy dodać kod obsługi wykonanego zdjęcia
                }
            }
    );


    public void takePicture() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Brak uprawnień do aparatu, należy poprosić użytkownika o zgodę
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            // Wywołaj aktywność aparatu
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                takePictureLauncher.launch(takePictureIntent);
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometr, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
