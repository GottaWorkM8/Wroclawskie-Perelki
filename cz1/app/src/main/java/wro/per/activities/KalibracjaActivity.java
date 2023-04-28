package wro.per.activities;

import static java.lang.Math.abs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.net.ParseException;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import wro.per.R;

public class KalibracjaActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    TextView wyniki;
    TextView naZywo;

    ImageView pozycjaImageView;

    private float x, y, z;

    private float avgDriftX, avgDriftY, avgDriftZ;
    private float driftX1, driftX2, driftY1, driftY2;

    private int aktualnypomiar = 1;

    private Boolean otwarcieAplikacji = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.kalibracja_layout);

        Intent intent = getIntent();
        otwarcieAplikacji = intent.getBooleanExtra("otwarcieAplikacji", true);
        System.out.println("otwarcie aplikacji: "+otwarcieAplikacji);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        wyniki = findViewById(R.id.driftyTextView);
        pozycjaImageView = findViewById(R.id.phonePositionImageView);

        Button przejdzDalejButton;

        przejdzDalejButton = findViewById(R.id.nastepnyPomiarButton);
        przejdzDalejButton.setOnClickListener(view -> nastepnyPomiar());

        if(otwarcieAplikacji)
            readDate();
    }

    public void readDate() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        String dateString = sharedPreferences.getString("Last_calibration", "");
        System.out.println("Pobrana data: " + dateString);

        if (!dateString.isEmpty()) {
            System.out.println("Pobrana data nie jest pusta");
            String pobranyRok = dateString.substring(0, 4);
            String pobranyMsc = dateString.substring(5, 7);
            String pobranyDzien = dateString.substring(8, 10);

            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateNow = dateFormat.format(date);

            String nowRok = dateNow.substring(0, 4);
            String nowMsc = dateNow.substring(5, 7);
            String nowDzien = dateNow.substring(8, 10);

            System.out.println("Pobrana data fragmenty: "+pobranyRok+" "+pobranyMsc+" "+pobranyDzien);
            System.out.println("Aktualna data fragmenty: "+nowRok+" "+nowMsc+" "+nowDzien);

            if (pobranyRok.equals(nowRok) && pobranyMsc.equals(nowMsc) && pobranyDzien.equals(nowDzien)) {
                System.out.println("Daty są te same");
                Intent intent = new Intent(KalibracjaActivity.this, StronaGlownaActivity.class);
                startActivity(intent);
                finish();
            } else {
                System.out.println("Daty nie są te same");
                writeDate();
            }
        } else
        {
            System.out.println("Pobrana data jest pusta");
            writeDate();
        }

    }

    public void writeDate() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = dateFormat.format(date);
        System.out.println("Zapisuje datę: "+dateString);

        editor.putString("Last_calibration", dateString);
        editor.apply();
        System.out.println("Zapisano date");

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void nastepnyPomiar() {
        switch (aktualnypomiar) {
            case 1:
                avgDriftZ = 9.81f - z;
                wyniki.setText("Odczytany drift: " + avgDriftZ);
                pozycjaImageView.setImageResource(R.drawable.on_bottom_icon);
                aktualnypomiar++;
                break;
            case 2:
                driftY1 = 9.81f - y;
                wyniki.setText("Odczytany drift: " + driftY1);
                pozycjaImageView.setImageResource(R.drawable.on_right_icon);
                aktualnypomiar++;
                break;
            case 3:
                driftX1 = 9.81f - abs(x);
                wyniki.setText("Odczytany drift: " + driftX1);
                pozycjaImageView.setImageResource(R.drawable.on_top_icon);
                aktualnypomiar++;
                break;
            case 4:
                driftY2 = 9.81f - abs(y);
                avgDriftY = (driftY1 + driftY2) / 2;
                wyniki.setText("Odczytany drift: " + avgDriftY);
                pozycjaImageView.setImageResource(R.drawable.on_left_icon);
                aktualnypomiar++;
                break;
            case 5:
                driftX2 = 9.81f - x;
                avgDriftX = (driftX1 + driftX2) / 2;
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("driftX", String.valueOf(avgDriftX));
                editor.putString("driftY", String.valueOf(avgDriftY));
                editor.putString("driftZ", String.valueOf(avgDriftZ));
                editor.apply();

                wyniki.setText("Wyniki pomiarów:\nPrzesunięcie X: " + avgDriftX + "\nPrzesunięcie Y: " + avgDriftY + "\nPrzesunięcie Z: " + avgDriftZ);
                aktualnypomiar++;
                TextView prompt = findViewById(R.id.promptTextView);
                prompt.setText("Kalibracja zakończona");
                //pozycjaImageView.setImageDrawable(null);
                pozycjaImageView.setVisibility(View.GONE);
                Button przejdzDalejButton;
                przejdzDalejButton = findViewById(R.id.nastepnyPomiarButton);
                przejdzDalejButton.setText("Zakończ");
                break;
            case 6:

                if(otwarcieAplikacji) {
                    Intent intent = new Intent(KalibracjaActivity.this, StronaGlownaActivity.class);
                    startActivity(intent);
                }
                finish();

                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

//            naZywo.setText("X: " + String.format("%.2f", x) + "\nY: " + String.format("%.2f", y) + "\nZ: " + String.format("%.2f", z));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
