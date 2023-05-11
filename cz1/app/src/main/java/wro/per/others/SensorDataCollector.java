package wro.per.others;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorDataCollector implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Sensor magnetometerSensor;
    private Sensor rotationSensor;
    private float[] lastAccelerometerValues = new float[3];
    private float[] lastMagnetometerValues = new float[3];
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];
    private float[] rotation = new float[3];

    private float direction=0;

    private float azimuth = 0.0f;
    private float pitch = 0.0f;
    private float roll = 0.0f;

    private float driftX;
    private float driftY;
    private float driftZ;

    public SensorDataCollector(Context context) {
        // Inicjalizacja SensorManagera i czujników
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        driftX = Float.parseFloat(sharedPreferences.getString("driftX", "0f"));
        driftY = Float.parseFloat(sharedPreferences.getString("driftY", "0f"));
        driftZ = Float.parseFloat(sharedPreferences.getString("driftZ", "0f"));
    }

    public void start() {
        // Rejestracja SensorEventListenera
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        // Wyrejestrowanie SensorEventListenera
        sensorManager.unregisterListener(this);
    }

    public float getAzimuth() {
        return azimuth;
    }

    public float getPitch() {
        return pitch;
    }

    public float getRoll() {
        return roll;
    }

    public float[] getAccelerometrValues()
    {
        return lastAccelerometerValues;
    }

    public float getDirection(){return direction;}

    @Override
    public void onSensorChanged(SensorEvent event) {

        // Sprawdzenie, czy zdarzenie pochodzi z czujnika akcelerometru
        if (event.sensor == accelerometerSensor) {
            System.arraycopy(event.values, 0, lastAccelerometerValues, 0, event.values.length);
            lastAccelerometerValues[0]+=driftX;
            lastAccelerometerValues[1]+=driftY;
            lastAccelerometerValues[2]+=driftZ;
        }

        // Sprawdzenie, czy zdarzenie pochodzi z czujnika magnetometru
        if (event.sensor == magnetometerSensor) {
            System.arraycopy(event.values, 0, lastMagnetometerValues, 0, event.values.length);
        }

        // Sprawdzenie, czy zdarzenie pochodzi z czujnika magnetometru
        if (event.sensor == rotationSensor) {
            rotation[0] = event.values[0];
            rotation[1] = event.values[1];
            rotation[2] = event.values[2];
        }

        direction = (rotation[2]+1)/2*360;

        // Aktualizacja macierzy obrotu i obliczenie azymutu, pitchu i rollu
        SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometerValues, lastMagnetometerValues);
        SensorManager.getOrientation(rotationMatrix, orientation);

        azimuth = Math.abs((float) Math.toDegrees(orientation[0]));
        pitch = Math.abs((float) Math.toDegrees(orientation[1]));
        roll = Math.abs((float) Math.toDegrees(orientation[2]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Metoda wymagana przez interfejs SensorEventListenera, ale w tym przykładzie nie jest używana
    }
}