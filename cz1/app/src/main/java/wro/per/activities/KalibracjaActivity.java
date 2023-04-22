package wro.per.activities;

import static java.lang.Math.abs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import wro.per.R;

public class KalibracjaActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    TextView wyniki;
    TextView naZywo;

    private float x,y,z;

    private float avgDriftX, avgDriftY, avgDriftZ;
    private float driftX1, driftX2, driftY1, driftY2;

    private int aktualnypomiar = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.kalibracja_layout);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        wyniki = findViewById(R.id.driftyTextView);
        naZywo = findViewById(R.id.odczytTextView);

        Button przejdzDalejButton;

        przejdzDalejButton = findViewById(R.id.nastepnyPomiarButton);
        przejdzDalejButton.setOnClickListener(view -> nastepnyPomiar());
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void nastepnyPomiar() {
        switch (aktualnypomiar)
        {
            case 1:
                avgDriftZ = abs(9.81f - z);
                wyniki.setText("Odczytany drift: "+avgDriftZ);
                aktualnypomiar++;
                break;
            case 2:
                driftY1 = abs(9.81f - y);
                wyniki.setText("Odczytany drift: "+driftY1);
                aktualnypomiar++;
                break;
            case 3:
                driftX1 = abs(9.81f - x);
                wyniki.setText("Odczytany drift: "+driftX1);
                aktualnypomiar++;
                break;
            case 4:
                driftY2 = abs(9.81f - y);
                avgDriftY = (driftY1+driftY2)/2;
                wyniki.setText("Odczytany drift: "+avgDriftY);
                aktualnypomiar++;
                break;
            case 5:
                driftX2 = abs(9.81f - y);
                avgDriftX = (driftX1+driftX2)/2;
                wyniki.setText("Wyniki pomiarów:\nPrzesunięcie X: "+avgDriftX+"\nPrzesunięcie Y: "+avgDriftY+"\nPrzesunięcie Z: "+avgDriftZ);
                aktualnypomiar++;
                Button przejdzDalejButton;
                przejdzDalejButton = findViewById(R.id.nastepnyPomiarButton);
                przejdzDalejButton.setText("Zakończ");
                break;
            case 6:
                Intent intent = new Intent(KalibracjaActivity.this, StronaGlownaActivity.class);
                startActivity(intent);
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
