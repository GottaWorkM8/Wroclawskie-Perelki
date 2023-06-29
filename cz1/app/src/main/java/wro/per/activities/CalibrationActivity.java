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
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import wro.per.R;

public class CalibrationActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    TextView resultsTextView;

    ImageView positionImageView;

    private float x, y, z;

    private float avgDriftX, avgDriftY, avgDriftZ;
    private float driftX1, driftX2, driftY1, driftY2;

    private int currentMeasurement = 1;

    private Boolean openingApp = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.calibration_layout);

        Intent intent = getIntent();
        openingApp = intent.getBooleanExtra("otwarcieAplikacji", true);
        System.out.println("otwarcie aplikacji: "+ openingApp);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        resultsTextView = findViewById(R.id.driftyTextView);
        positionImageView = findViewById(R.id.phonePositionImageView);

        Button goNextButton;

        goNextButton = findViewById(R.id.nastepnyPomiarButton);
        goNextButton.setOnClickListener(view -> nextMeasurement());

        if(openingApp)
            readDate();
    }

    public void readDate() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        String dateString = sharedPreferences.getString("Last_calibration", "");
        System.out.println("Pobrana data: " + dateString);

        if (!dateString.isEmpty()) {
            System.out.println("Pobrana data nie jest pusta");
            String collectedYear = dateString.substring(0, 4);
            String collecterMonth = dateString.substring(5, 7);
            String collectedDay = dateString.substring(8, 10);

            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateNow = dateFormat.format(date);

            String nowYear = dateNow.substring(0, 4);
            String nowMonth = dateNow.substring(5, 7);
            String nowDay = dateNow.substring(8, 10);

//            System.out.println("Pobrana data fragmenty: "+collectedYear+" "+collecterMonth+" "+collectedDay);
//            System.out.println("Aktualna data fragmenty: "+nowYear+" "+nowMonth+" "+nowDay);

            if (collectedYear.equals(nowYear) && collecterMonth.equals(nowMonth) && collectedDay.equals(nowDay)) {
//                System.out.println("Daty są te same");
                Intent intent = new Intent(CalibrationActivity.this, MainPageActivity.class);
                startActivity(intent);
                finish();
            } else {
//                System.out.println("Daty nie są te same");
                writeDate();
            }
        } else
        {
//            System.out.println("Pobrana data jest pusta");
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
//        System.out.println("Zapisuje datę: "+dateString);

        editor.putString("Last_calibration", dateString);
        editor.apply();
//        System.out.println("Zapisano date");

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void nextMeasurement() {
        switch (currentMeasurement) {
            case 1:
                avgDriftZ = 9.81f - z;
//                resultsTextView.setText("Odczytany drift: " + avgDriftZ);
                positionImageView.setImageResource(R.drawable.black_on_bottom_icon);
                currentMeasurement++;
                break;
            case 2:
                driftY1 = 9.81f - y;
//                resultsTextView.setText("Odczytany drift: " + driftY1);
                positionImageView.setImageResource(R.drawable.black_on_right_icon);
                currentMeasurement++;
                break;
            case 3:
                driftX1 = 9.81f - abs(x);
//                resultsTextView.setText("Odczytany drift: " + driftX1);
                positionImageView.setImageResource(R.drawable.black_on_top_icon);
                currentMeasurement++;
                break;
            case 4:
                driftY2 = 9.81f - abs(y);
                avgDriftY = (driftY1 + driftY2) / 2;
//                resultsTextView.setText("Odczytany drift: " + avgDriftY);
                positionImageView.setImageResource(R.drawable.black_on_left_icon);
                currentMeasurement++;
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

//                resultsTextView.setText("Wyniki pomiarów:\nPrzesunięcie X: " + avgDriftX + "\nPrzesunięcie Y: " + avgDriftY + "\nPrzesunięcie Z: " + avgDriftZ);
                currentMeasurement++;
                TextView prompt = findViewById(R.id.promptTextView);
                prompt.setText("Kalibracja zakończona");
                //pozycjaImageView.setImageDrawable(null);
                positionImageView.setVisibility(View.GONE);
                Button goNextButton;
                goNextButton = findViewById(R.id.nastepnyPomiarButton);
                goNextButton.setText("Zakończ");
                break;
            case 6:

                if(openingApp) {
                    Intent intent = new Intent(CalibrationActivity.this, MainPageActivity.class);
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
