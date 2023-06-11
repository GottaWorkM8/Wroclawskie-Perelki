package wro.per.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import wro.per.R;
import wro.per.others.ApiRequestTask;
import wro.per.others.JsonListReceiver;

public class FavouritesActivity extends AppCompatActivity implements JsonListReceiver.JsonReceiverListener, ApiRequestTask.ApiResponseListener {

    ImageButton profilButton, homeButton, solvedButton, infoButton;
    LinearLayout favouritesList;
    SharedPreferences sharedPreferences;
    String userKey;

    ArrayList<ArrayList<String>> favouritesObjects = new ArrayList<>();

    @Override
    public void onJsonReceived(List<JSONObject> jsonObjects) {
        if (jsonObjects == null) {
            Toast.makeText(this, "Nie udało się pobrać danych", Toast.LENGTH_SHORT).show();
        } else {
            try {
                for(JSONObject object : jsonObjects)
                {
                    favouritesObjects.add(new ArrayList<>());
                    favouritesObjects.get(favouritesObjects.size()-1).add(String.valueOf(object.getInt("id")));
                    favouritesObjects.get(favouritesObjects.size()-1).add(object.getString("objectName"));
                    favouritesObjects.get(favouritesObjects.size()-1).add(object.getString("photoObjectLink"));
                    favouritesObjects.get(favouritesObjects.size()-1).add(object.getString("infoLink"));
                }
                showFavourites();
            }catch (Exception e)
            {
                Toast.makeText(this, "Błąd podczas odczytywania danych", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourites_layout);

        favouritesList = findViewById(R.id.favouritesList);

        profilButton = findViewById(R.id.profileButton);
        profilButton.setOnClickListener(view -> openProfileActivity());

        solvedButton = findViewById(R.id.favouriteButton);
        solvedButton.setOnClickListener(view -> openSolvedActivity());

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> openMainPageActivity());

        infoButton = findViewById(R.id.settingsButton);
        infoButton.setOnClickListener(view -> openInfoActivity());

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        userKey = sharedPreferences.getString("userKey", "defaultKey");

        String apiUrl = "https://szajsjem.mooo.com/api/user/ulubioneMiejsca?key="+userKey;
        JsonListReceiver jsonListReceiver = new JsonListReceiver(this);
        jsonListReceiver.execute(apiUrl);
    }

    public void showFavourites()
    {
        for (int i = 0; i < favouritesObjects.size(); i++) {
            View tile = getLayoutInflater().inflate(R.layout.favourite_tile, null, false);
            TextView name = tile.findViewById(R.id.favouriteName);
            name.setText(favouritesObjects.get(i).get(1));
            int finalI = i;
            tile.setOnClickListener(view -> {
                Intent intent = new Intent(this, ObjectInfoActivity.class);
                intent.putExtra("infoURL", favouritesObjects.get(finalI).get(3));
                System.out.println("Link do strony: "+favouritesObjects.get(finalI).get(3));
                startActivity(intent);
            });

            ImageView starIcon = tile.findViewById(R.id.starIcon);
            starIcon.setOnClickListener(view -> {
                String URL = "https://szajsjem.mooo.com/api/user/ulubioneMiejsca?key="+userKey;
                ApiRequestTask apiRequestTask = new ApiRequestTask(URL, "POST", "{\"id\":\"" + favouritesObjects.get(finalI).get(0) + "\"}", this);
                apiRequestTask.execute();
            });

            favouritesList.addView(tile);
        }
    }

    public void openProfileActivity() {
        Intent intent = new Intent(this, ProfilActivity.class);
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

    public void openInfoActivity() {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onApiResponse(String response) {
        Toast.makeText(this, "Usunięto z ulubionych", Toast.LENGTH_SHORT).show();
        recreate();
    }
}