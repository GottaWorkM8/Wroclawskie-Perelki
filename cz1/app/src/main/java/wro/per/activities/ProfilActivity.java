package wro.per.activities;

import static java.lang.Math.abs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;



import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

import wro.per.R;
import wro.per.others.Camera;

public class ProfilActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor accelerometr;

    private float x, y, z;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edycja_obiektu_layout);

        Button wykonajZdjecieButton;

        wykonajZdjecieButton = findViewById(R.id.zdjecie_szczegolu_button);

        wykonajZdjecieButton.setOnClickListener(view -> {
                    Intent intent = new Intent(this, KameraActivity.class);
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometr = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            String imagePath = data.getStringExtra("imagePath");

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
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
