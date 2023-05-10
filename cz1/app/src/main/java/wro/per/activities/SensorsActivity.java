package wro.per.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import wro.per.R;

public class SensorsActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor getAccelerometerLinear;
    private Sensor gyroscope;
    private Sensor gravitySensor;
    private Sensor rotationVector;
    private Sensor gameRotationVector;
    private Sensor geoMagrotationVector;
    private Sensor magSensor;
    private Sensor orientVector;
    private TextView xValueAcc, yValueAcc, zValueAcc;
    private TextView xValueAccLin, yValueAccLin, zValueAccLin;
    private TextView xValueGyro, yValueGyro, zValueGyro;
    private TextView xValueGrav, yValueGrav, zValueGrav;
    private TextView xValueRot, yValueRot, zValueRot;
    private TextView xValueRotGame, yValueRotGame, zValueRotGame;
    private TextView xValueRotGeoMag, yValueRotGeoMag, zValueRotGeoMag;
    private TextView xValueMag, yValueMag, zValueMag;
    private TextView xValueOrient, yValueOrient, zValueOrient;

    private Float driftX, driftZ, driftY;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_layout);

        xValueAcc = (TextView) findViewById(R.id.accXTextView);
        yValueAcc = (TextView) findViewById(R.id.accYTextView);
        zValueAcc = (TextView) findViewById(R.id.accZTextView);

        xValueAccLin = (TextView) findViewById(R.id.accLinXTextView);
        yValueAccLin = (TextView) findViewById(R.id.accLinYTextView);
        zValueAccLin = (TextView) findViewById(R.id.accLinZTextView);

        xValueGyro = (TextView) findViewById(R.id.gyroXTextView);
        yValueGyro = (TextView) findViewById(R.id.gyroYTextView);
        zValueGyro = (TextView) findViewById(R.id.gyroZTextView);

        xValueGrav = (TextView) findViewById(R.id.gravXTextView);
        yValueGrav = (TextView) findViewById(R.id.gravYTextView);
        zValueGrav = (TextView) findViewById(R.id.gravZTextView);

        xValueRot = (TextView) findViewById(R.id.rotXTextView);
        yValueRot = (TextView) findViewById(R.id.rotYTextView);
        zValueRot = (TextView) findViewById(R.id.rotZTextView);

        xValueRotGame = (TextView) findViewById(R.id.gameRotXTextView);
        yValueRotGame = (TextView) findViewById(R.id.gameRotYTextView);
        zValueRotGame = (TextView) findViewById(R.id.gameRotZTextView);

        xValueRotGeoMag = (TextView) findViewById(R.id.geoMagRotXTextView);
        yValueRotGeoMag = (TextView) findViewById(R.id.geoMagRotYTextView);
        zValueRotGeoMag = (TextView) findViewById(R.id.geoMagRotZTextView);

        xValueMag = (TextView) findViewById(R.id.magXTextView);
        yValueMag = (TextView) findViewById(R.id.magYTextView);
        zValueMag = (TextView) findViewById(R.id.magZTextView);

        xValueOrient = (TextView) findViewById(R.id.orientXTextView);
        yValueOrient = (TextView) findViewById(R.id.orientYTextView);
        zValueOrient = (TextView) findViewById(R.id.orientZTextView);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        getAccelerometerLinear = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        rotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        gameRotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        geoMagrotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);
        magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        orientVector = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        driftX = Float.valueOf(sharedPreferences.getString("driftX", "0f"));
        driftY = Float.valueOf(sharedPreferences.getString("driftY", "0f"));
        driftZ = Float.valueOf(sharedPreferences.getString("driftZ", "0f"));

        final ImageButton otworzStroneGlownaButton;

        otworzStroneGlownaButton = (ImageButton) findViewById(R.id.homeButton);
        otworzStroneGlownaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomeActivity();
            }
        });
    }

    public void openHomeActivity() {
        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, getAccelerometerLinear, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, rotationVector, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gameRotationVector, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, geoMagrotationVector, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, orientVector, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0] + driftX;
            float y = event.values[1] + driftY;
            float z = event.values[2] + driftZ;

            xValueAcc.setText("X: " + String.format("%.2f", x));
            yValueAcc.setText("Y: " + String.format("%.2f", y));
            zValueAcc.setText("Z: " + String.format("%.2f", z));
        } else if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            xValueAccLin.setText("X: " + String.format("%.2f", x));
            yValueAccLin.setText("Y: " + String.format("%.2f", y));
            zValueAccLin.setText("Z: " + String.format("%.2f", z));
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            xValueGyro.setText("X: " + String.format("%.2f", x));
            yValueGyro.setText("Y: " + String.format("%.2f", y));
            zValueGyro.setText("Z: " + String.format("%.2f", z));
        } else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            xValueGrav.setText("X: " + String.format("%.2f", x));
            yValueGrav.setText("Y: " + String.format("%.2f", y));
            zValueGrav.setText("Z: " + String.format("%.2f", z));
        } else if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            xValueRot.setText("X: " + String.format("%.2f", x));
            yValueRot.setText("Y: " + String.format("%.2f", y));
            zValueRot.setText("Z: " + String.format("%.2f", z));
        } else if (event.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            xValueRotGame.setText("X: " + String.format("%.2f", x));
            yValueRotGame.setText("Y: " + String.format("%.2f", y));
            zValueRotGame.setText("Z: " + String.format("%.2f", z));
        } else if (event.sensor.getType() == Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            xValueRotGeoMag.setText("X: " + String.format("%.2f", x));
            yValueRotGeoMag.setText("Y: " + String.format("%.2f", y));
            zValueRotGeoMag.setText("Z: " + String.format("%.2f", z));
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            xValueMag.setText("X: " + String.format("%.2f", x));
            yValueMag.setText("Y: " + String.format("%.2f", y));
            zValueMag.setText("Z: " + String.format("%.2f", z));
        } else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            xValueOrient.setText("X: " + String.format("%.2f", x));
            yValueOrient.setText("Y: " + String.format("%.2f", y));
            zValueOrient.setText("Z: " + String.format("%.2f", z));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
