package wro.per.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import wro.per.R;

public class InfoActivity extends AppCompatActivity {

    Button sensorsButton, calibrationButton, editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_layout);

        sensorsButton = findViewById(R.id.sensory_button);
        calibrationButton = findViewById(R.id.kalibracja_botton);
        editButton = findViewById(R.id.edycja_obiektu_button);

        sensorsButton.setOnClickListener(view -> openSensors());
        calibrationButton.setOnClickListener(view -> openCalibration());
        editButton.setOnClickListener(view -> openEdit());
    }

    private void openSensors()
    {
        Intent intent = new Intent(this, SensorsActivity.class);
        startActivity(intent);
        finish();
    }

    private void openCalibration()
    {
        Intent intent = new Intent(this, CalibrationActivity.class);
        intent.putExtra("otwarcieAplikacji", false);
        startActivity(intent);
        finish();
    }

    private void openEdit()
    {
        Intent intent = new Intent(this, EditObjectActivity.class);
        startActivity(intent);
        finish();
    }

}