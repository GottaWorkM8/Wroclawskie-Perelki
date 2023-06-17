package wro.per.activities;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import wro.per.R;

public class ObjectInfoActivity extends AppCompatActivity {

    WebView infoWebView;

    ImageButton profilButton, homeButton, favouritesButton, infoButton;

    TextView objectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.object_info_layout);

        objectName = findViewById(R.id.object_name);

        profilButton = findViewById(R.id.profileButton);
        profilButton.setOnClickListener(view -> openProfileActivity());

        favouritesButton = findViewById(R.id.favouriteButton);
        favouritesButton.setOnClickListener(view -> openFavouritesActivity());

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> openMainPageActivity());

        infoButton = findViewById(R.id.settingsButton);
        infoButton.setOnClickListener(view -> openInfoActivity());

        Intent intent = getIntent();
        String URL = intent.getStringExtra("infoURL");
        String name = intent.getStringExtra("objectName");
        objectName.setText(name);

        infoWebView = findViewById(R.id.infoWebView);
        infoWebView.getSettings().setJavaScriptEnabled(true);

        infoWebView.loadUrl(URL);
    }

    public void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
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
