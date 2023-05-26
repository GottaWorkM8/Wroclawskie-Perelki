package wro.per.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import wro.per.R;

public class ProfilActivity extends AppCompatActivity {

    TextView loginTextView, activityDateTextView, pointsTextView, objectsTextView, riddlesTextView, easyTextView, mediumTextView, hardTextView;
    Button logoutButton;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_layout);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setIds();
        setLogin();
    }

    private void setLogin()
    {
        String login = sharedPreferences.getString("userLogin", "Login");
        loginTextView.setText(login);
    }

    private void setIds()
    {
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

    private void logOut()
    {
        editor.putString("userLogin", "");
        editor.putString("userPass", "");
        editor.putBoolean("zalogowano", false);
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}