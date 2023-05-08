package wro.per.activities;

import static java.lang.Math.abs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import wro.per.R;
import wro.per.others.LocationService;

public class ProfilActivity extends AppCompatActivity {

    private EditText nachylenie, detailCoords;
    float nachylenieX, nachylenieY, nachylenieZ;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    double  currLatitude, currLongitude, latitude, longitude, detailLatitude, detailLongitude;

    EditText wspolrzedneText;


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {
            wspolrzedneText = findViewById(R.id.wspolrzedne_obserwacji_edittext);

            latitude = intent.getDoubleExtra("latitude", 3);
            longitude = intent.getDoubleExtra("longitude", 3);


        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edycja_obiektu_layout);

        Button wykonajZdjecieButton;

        Button wysliljDoBazyButton;

        Button zaladujWspolrzedne;

        nachylenie = findViewById(R.id.nachylenieEditText);

        detailCoords = findViewById(R.id.wspolrzedne_szczegołu_edittext);

        zaladujWspolrzedne = findViewById(R.id.wspolrzedne_obserwacji_button);

        wykonajZdjecieButton = findViewById(R.id.zdjecie_szczegolu_button);

        wysliljDoBazyButton = findViewById(R.id.wyslij_button);

        wykonajZdjecieButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, KameraActivity.class);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        });


        zaladujWspolrzedne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilActivity.this, LocationService.class);
                startService(intent);

                currLatitude = latitude;
                currLongitude = longitude;

                wspolrzedneText.setText(currLatitude + ",  " + currLongitude);

            }
        });

        wysliljDoBazyButton.setOnClickListener(view -> {
            Toast.makeText(this, "Jeszcze nie działa", Toast.LENGTH_LONG).show();
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            String imagePath = data.getStringExtra("imagePath");
            nachylenieX = data.getFloatExtra("nachylenieX", 0);
            nachylenieY = data.getFloatExtra("nachylenieY", 0);
            nachylenieZ = data.getFloatExtra("nachylenieZ", 0);

            nachylenie.setText(Float.toString(nachylenieX));

            detailLatitude = data.getDoubleExtra("lat",1);
            detailLongitude = data.getDoubleExtra("lon", 1);

            detailCoords.setText(Double.toString(detailLatitude)+ ",  " + Double.toString(detailLongitude));

            System.out.println("Nachylenie:");
            System.out.println("X: "+nachylenieX);
            System.out.println("Y: "+nachylenieY);
            System.out.println("Z: "+nachylenieZ);

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
    protected void onStop()
    {
        super.onStop();
        stopLocationService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationService();
        unregisterReceiver(broadcastReceiver);
    }

}
