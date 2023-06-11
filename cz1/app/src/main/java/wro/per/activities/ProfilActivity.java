package wro.per.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import wro.per.R;
import wro.per.others.Places;

public class ProfilActivity extends AppCompatActivity {

    TextView loginTextView, activityDateTextView, pointsTextView, objectsTextView, riddlesTextView, easyTextView, mediumTextView, hardTextView;
    Button logoutButton;

    ImageButton solvedButton, homeButton, favouritesButton, infoButton;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_layout);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        solvedButton = findViewById(R.id.listButton);
        solvedButton.setOnClickListener(view -> openSolvedActivity());

        favouritesButton = findViewById(R.id.favouriteButton);
        favouritesButton.setOnClickListener(view -> openFavouritesActivity());

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view->openMainPageActivity());

        infoButton = findViewById(R.id.settingsButton);
        infoButton.setOnClickListener(view-> openInfoActivity());

        setIds();
        setLogin();
        Places places = Places.getInstance();
        System.out.println(places.getPlaces());
    }

    private void setLogin() {
        String login = sharedPreferences.getString("userLogin", "Login");
        loginTextView.setText(login);
    }

    private void setIds() {
        loginTextView = findViewById(R.id.loginTextView);
        activityDateTextView = findViewById(R.id.activity_date_textView);
        pointsTextView = findViewById(R.id.points_get_textView);
        objectsTextView = findViewById(R.id.objects_found_textView);
        riddlesTextView = findViewById(R.id.riddles_solved_textView);
        easyTextView = findViewById(R.id.easy_solved_textview);
        mediumTextView = findViewById(R.id.medium_solved_textview);
        hardTextView = findViewById(R.id.hard_solved_textview);
        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(view -> logOut());


    }

    private void logOut() {
        editor.putString("userLogin", "");
        editor.putString("userPass", "");
        editor.putBoolean("zalogowano", false);
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void openSolvedActivity() {
        Intent intent = new Intent(this, SolvedActivityOld.class);
        startActivity(intent);
        finish();
    }

    public void openMainPageActivity() {
        finish();
    }

    public void openFavouritesActivity() {
        Intent intent = new Intent(this, FavouritesActivity.class);
        startActivity(intent);
        finish();
    }

    public void openInfoActivity() {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
        finish();
    }
}