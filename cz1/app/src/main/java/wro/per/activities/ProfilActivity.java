package wro.per.activities;

import static java.lang.Math.abs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import wro.per.R;

public class ProfilActivity extends AppCompatActivity {

    private EditText nachylenie;
    float nachylenieX, nachylenieY, nachylenieZ;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edycja_obiektu_layout);

        nachylenie = findViewById(R.id.nachylenieEditText);

        Button wykonajZdjecieButton;

        Button wysliljDoBazyButton;

        wykonajZdjecieButton = findViewById(R.id.zdjecie_szczegolu_button);
        wysliljDoBazyButton = findViewById(R.id.wyslij_button);

        wykonajZdjecieButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, KameraActivity.class);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
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



}
