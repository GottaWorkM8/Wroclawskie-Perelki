package wro.per.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wro.per.R;
import wro.per.others.JsonListReceiver;

public class NotStartedActivity extends AppCompatActivity implements JsonListReceiver.JsonReceiverListener {

    ImageButton profilButton, homeButton, favouritesButton, infoButton;
    Button inProgress, notStarted;

    List<JSONObject> notStartedRiddles = new ArrayList<>();

    LinearLayout notStartedList;

    @Override
    public void onJsonReceived(List<JSONObject> jsonObjects) {
        if (jsonObjects == null) {
            Toast.makeText(this, "Brak rozwiązanych zagadek", Toast.LENGTH_SHORT).show();
        } else {
            notStartedRiddles.addAll(jsonObjects);
            showNotStarted();
        }
    }

    private void showNotStarted() {
        for (int i = 0; i < notStartedRiddles.size(); i++) {
            try {
                JSONObject object = notStartedRiddles.get(i);
                View tile = getLayoutInflater().inflate(R.layout.riddle_tile_fragment, null, false);
                String riddleName = object.getString("name");
                TextView name = tile.findViewById(R.id.name);
                name.setText("nieodblokowane (" + riddleName + ")");
                TextView objectCount = tile.findViewById(R.id.objectCount);
                objectCount.setText(object.getString("objectCount"));
                int riddleId = object.getInt("id");
                tile.setOnClickListener(view -> {
                    Intent intent = new Intent(this, ObjectListActivity.class);
                    intent.putExtra("riddleID", riddleId);
                    intent.putExtra("riddleName", riddleName);
                    startActivity(intent);
                });
                notStartedList.addView(tile);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_started_layout);

        profilButton = findViewById(R.id.profileButton);
        profilButton.setOnClickListener(view -> openProfileActivity());

        favouritesButton = findViewById(R.id.favouriteButton);
        favouritesButton.setOnClickListener(view -> openFavouritesActivity());

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> openMainPageActivity());

        infoButton = findViewById(R.id.settingsButton);
        infoButton.setOnClickListener(view -> openInfoActivity());

        inProgress = findViewById(R.id.wTrakcieButton);
        inProgress.setOnClickListener(view -> openInPorgressActivity());

        notStarted = findViewById(R.id.nierozpoczeteButton);
        notStarted.setOnClickListener(view -> openNotStartedActivity());

        notStartedList = findViewById(R.id.notStartedList);

        String apiUrl = "https://szajsjem.mooo.com/api/zagadka";
        JsonListReceiver jsonListReceiver = new JsonListReceiver(this);
        jsonListReceiver.execute(apiUrl);
    }

    private void openNotStartedActivity() {
    }

    private void openInPorgressActivity() {
    }

    public void openProfileActivity() {
        Intent intent = new Intent(this, ProfilActivity.class);
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