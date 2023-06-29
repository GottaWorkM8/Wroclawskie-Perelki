package wro.per.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import wro.per.R;

public class InfoActivity extends AppCompatActivity {

    Button sensorsButton, calibrationButton, editButton;

    ImageButton profilButton, homeButton, solvedButton, favouritesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_layout);


        calibrationButton = findViewById(R.id.kalibracja_botton);
        editButton = findViewById(R.id.edycja_obiektu_button);

        profilButton = findViewById(R.id.profileButton);
        profilButton.setOnClickListener(view -> openProfileActivity());

        solvedButton = findViewById(R.id.listButton);
        solvedButton.setOnClickListener(view -> openSolvedActivity());

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view->openMainPageActivity());

        favouritesButton = findViewById(R.id.favouriteButton);
        favouritesButton.setOnClickListener(view-> openFavouritesActivity());

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

    public void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    public void openMainPageActivity() {
        finish();
    }

    public void openSolvedActivity() {
        Intent intent = new Intent(this, SolvedActivity.class);
        startActivity(intent);
        finish();
    }

    public void openFavouritesActivity() {
        Intent intent = new Intent(this, FavouritesActivity.class);
        startActivity(intent);
        finish();
    }

}