package wro.per.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import wro.per.R;
import wro.per.others.ApiRequestTask;
import wro.per.others.JsonListReceiver;
import wro.per.others.Place;
import wro.per.others.Places;

public class ObjectListActivity extends AppCompatActivity implements JsonListReceiver.JsonReceiverListener, ApiRequestTask.ApiResponseListener {

    TextView riddleNameTextView;

    int riddleId;

    SharedPreferences sharedPreferences;
    TextView errorTextView;
    LinearLayout foundLinearLayout;
    String userKey;

    Boolean foundObjectsBool = false;
    Boolean favourites = true;

    ArrayList<JSONObject> favouritesObjects = new ArrayList<>();
    ArrayList<JSONObject> foundObjects = new ArrayList<>();
    ArrayList<JSONObject> notFoundObjects = new ArrayList<>();

    ImageButton profilButton, homeButton, favouritesButton, infoButton;

    @Override
    public void onJsonReceived(List<JSONObject> jsonObjects) {
        if(favourites)
        {
            favouritesObjects.addAll(jsonObjects);
            foundObjectsBool=true;
            favourites=false;
            String apiUrl = "https://szajsjem.mooo.com/api/zagadka/" + riddleId + "/znalezioneObiekty?key=" + userKey;
            JsonListReceiver jsonListReceiver = new JsonListReceiver(this);
            jsonListReceiver.execute(apiUrl);
        }
        else if (foundObjectsBool) {
            foundObjects.addAll(jsonObjects);
            foundObjectsBool = false;
            String apiUrl = "https://szajsjem.mooo.com/api/zagadka/" + riddleId + "/nieZnalezioneObiekty?key=" + userKey;
            JsonListReceiver jsonListReceiver = new JsonListReceiver(this);
            jsonListReceiver.execute(apiUrl);
        } else {
            notFoundObjects.addAll(jsonObjects);
            showFound();
        }
    }

    private boolean findInFavourites(int id)
    {
        for(int i=0; i<favouritesObjects.size();i++)
        {
            try {
                JSONObject object = favouritesObjects.get(i);
                int actualID = object.getInt("id");
                if(actualID==id)
                    return true;
            }catch (Exception e)
            {

            }
        }
        return false;
    }

    private void showFound() {
        if(foundObjects.size()==0)
        {
            errorTextView.setText("Brak znalezionych obiektów");

        }
        else {
            errorTextView.setVisibility(View.GONE);
            for (int i = 0; i < foundObjects.size(); i++) {
                try {
                    JSONObject object = foundObjects.get(i);
                    View tile = getLayoutInflater().inflate(R.layout.object_tile, null, false);
                    String riddleName = object.getString("objectName");
                    int id = object.getInt("id");
                    boolean inFavourites = findInFavourites(id);
                    TextView name = tile.findViewById(R.id.objectName);
                    name.setText(riddleName);
                    tile.setOnClickListener(view -> {
                        Intent intent = new Intent(this, ObjectInfoActivity.class);
                        try {
                            intent.putExtra("infoURL", object.getString("infoLink"));
                            intent.putExtra("objectName", object.getString("objectName"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        startActivity(intent);
                        finish();
                    });

                    ImageView starIcon = tile.findViewById(R.id.starIcon);
                    if(inFavourites)
                        starIcon.setImageResource(R.drawable.yellow_star_icon);
                    starIcon.setOnClickListener(view -> {
                        String URL = "https://szajsjem.mooo.com/api/user/ulubioneMiejsca?key="+userKey;
                        ApiRequestTask apiRequestTask = new ApiRequestTask(URL, "POST", "{\"id\":\"" + id + "\"}", this);
                        apiRequestTask.execute();
                    });

                    foundLinearLayout.addView(tile);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        showOnMap();

    }

    private void showOnMap() {
        Places places = Places.getInstance();

        ArrayList<Place> wroclawGeoPoints = new ArrayList<>();

        for (int i = 0; i < notFoundObjects.size(); i++) {
            try {
                JSONObject object = notFoundObjects.get(i);
                String observeCoords = object.getString("trackingPosition");
                String[] coords = observeCoords.split(",");
                float lati = Float.parseFloat(coords[0]);
                float longi = Float.parseFloat(coords[1]);
                wroclawGeoPoints.add(new Place(new GeoPoint(lati, longi), false, object));
                System.out.println("Nieznaleziony obiekt: " + object.getString("objectName"));

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        for (int i = 0; i < foundObjects.size(); i++) {
            try {
                JSONObject object = foundObjects.get(i);
                String observeCoords = object.getString("trackingPosition");
                String[] coords = observeCoords.split(",");
                Float lati = Float.parseFloat(coords[0]);
                Float longi = Float.parseFloat(coords[1]);
                wroclawGeoPoints.add(new Place(new GeoPoint(lati, longi), true, object));
                System.out.println("Znaleziony obiekt: " + object.getString("objectName"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        places.deletePlaces();
        places.setPlaces(wroclawGeoPoints);
        places.drawPlaces();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.object_list_layout);

        errorTextView = findViewById(R.id.errorTextView);

        profilButton = findViewById(R.id.profileButton);
        profilButton.setOnClickListener(view -> openProfileActivity());

        favouritesButton = findViewById(R.id.favouriteButton);
        favouritesButton.setOnClickListener(view -> openFavouritesActivity());

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> openMainPageActivity());

        infoButton = findViewById(R.id.settingsButton);
        infoButton.setOnClickListener(view -> openInfoActivity());

        Intent intent = getIntent();

        riddleId = intent.getIntExtra("riddleID", 0);
        if (intent.getBooleanExtra("showRiddleName", true)) {
            String riddleName = intent.getStringExtra("riddleName");
            riddleNameTextView = findViewById(R.id.riddleName);
            riddleNameTextView.setText(riddleName);
        }

        foundLinearLayout = findViewById(R.id.found);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        userKey = sharedPreferences.getString("userKey", "defaultKey");

        String apiUrl = "https://szajsjem.mooo.com/api/user/ulubioneMiejsca?key="+userKey;
        JsonListReceiver jsonListReceiver = new JsonListReceiver(this);
        jsonListReceiver.execute(apiUrl);
    }

    public void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    public void openMainPageActivity() {
//        Intent intent = new Intent();
//        intent.putExtra("close",true);
//        setResult(RESULT_OK, intent);
//        finish();

        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
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


    @Override
    public void onApiResponse(String response) {
        recreate();
    }
}