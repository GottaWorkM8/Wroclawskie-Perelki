package wro.per.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import wro.per.R;
import wro.per.others.JsonListReceiver;
import wro.per.others.Place;
import wro.per.others.Places;

public class ObjectListActivity extends AppCompatActivity implements JsonListReceiver.JsonReceiverListener {

    TextView riddleNameTextView;

    int riddleId;

    SharedPreferences sharedPreferences;

    LinearLayout foundLinearLayout;
    String userKey;

    Boolean allObjects = true;

    ArrayList<JSONObject> all = new ArrayList<>();
    ArrayList<JSONObject> found = new ArrayList<>();

    ArrayList<JSONObject> foundObjects = new ArrayList<>();
    ArrayList<JSONObject> notFoundObjects = new ArrayList<>();

    ImageButton profilButton, homeButton, favouritesButton, infoButton;

    @Override
    public void onJsonReceived(List<JSONObject> jsonObjects) {
        if (allObjects) {
            all.addAll(jsonObjects);
            allObjects = false;
            String apiUrl = "https://szajsjem.mooo.com/api/user/znalezioneMiejsca?key=" + userKey;
            JsonListReceiver jsonListReceiver = new JsonListReceiver(this);
            jsonListReceiver.execute(apiUrl);
        }
        else {
            found.addAll(jsonObjects);
            searchForNotFound();
        }
    }

    private void searchForNotFound() {

        // pobieramy ze znalezionych tylko te, które są z danej zagadki
        for (int i = 0; i < found.size(); i++) {
            try {
            JSONObject object = found.get(i);

                JSONObject riddle = object.getJSONObject("riddles");
                if(riddleId == riddle.getInt("id"))
                {
                    foundObjects.add(object);
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }

        // pobieramy te jeszcze nie znalezione
        for(int i=0;i<all.size();i++)
        {
            JSONObject object = all.get(i);
            Boolean isInAll = false;
            try {
                for(int j=0;j<foundObjects.size();j++)
                {
                    if(foundObjects.get(j).getInt("id") == object.getInt("id"))
                    {
                        isInAll = true;
                    }
                }
            } catch (JSONException e1) {
                throw new RuntimeException(e1);
            }
            if(!isInAll)
            {
                notFoundObjects.add(all.get(i));
            }
        }

        // wyświetlamy znalezione
        for(int i=0;i<foundObjects.size();i++)
        {
            try {
                JSONObject object = foundObjects.get(i);
                View tile = getLayoutInflater().inflate(R.layout.riddle_tile_fragment, null, false);
                String riddleName = object.getString("objectName");
                TextView name = tile.findViewById(R.id.name);
                name.setText(riddleName);
                tile.setOnClickListener(view -> {
                    Intent intent = new Intent(this, ObjectInfoActivity.class);
                    try {
                        intent.putExtra("infoURL", object.getString("infoLink"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    startActivity(intent);
                });

                foundLinearLayout.addView(tile);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        showOnMap();

    }

    private void showOnMap() {
        Places places = Places.getInstance();
//        List<Object> objectList = places.getObjectList();

        ArrayList<Place> wroclawGeoPoints = new ArrayList<>();

        for(int i=0;i<notFoundObjects.size();i++)
        {
            try {
                JSONObject object = notFoundObjects.get(i);
                String observeCoords = object.getString("trackingPosition");
                String[] coords = observeCoords.split(",");
                float lati = Float.parseFloat(coords[0]);
                float longi = Float.parseFloat(coords[1]);
                wroclawGeoPoints.add(new Place(new GeoPoint(lati, longi ), false));
                System.out.println("Nieznaleziony obiekt: "+object.getString("objectName"));

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        for(int i=0;i<foundObjects.size();i++)
        {
            try {
                JSONObject object = foundObjects.get(i);
                String observeCoords = object.getString("trackingPosition");
                String[] coords = observeCoords.split(",");
                Float lati = Float.parseFloat(coords[0]);
                Float longi = Float.parseFloat(coords[1]);
                wroclawGeoPoints.add(new Place(new GeoPoint(lati, longi ), true));
                System.out.println("Znaleziony obiekt: "+object.getString("objectName"));
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

        profilButton = findViewById(R.id.profileButton);
        profilButton.setOnClickListener(view -> openProfileActivity());

        favouritesButton = findViewById(R.id.favouriteButton);
        favouritesButton.setOnClickListener(view -> openFavouritesActivity());

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> openMainPageActivity());

        infoButton = findViewById(R.id.settingsButton);
        infoButton.setOnClickListener(view -> openInfoActivity());

        Intent intent = getIntent();
        String riddleName = intent.getStringExtra("riddleName");
        riddleId = intent.getIntExtra("riddleID", 0);
        riddleNameTextView = findViewById(R.id.riddleName);
        riddleNameTextView.setText(riddleName);

        foundLinearLayout = findViewById(R.id.found);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        userKey = sharedPreferences.getString("userKey", "defaultKey");

        String apiUrl = "https://szajsjem.mooo.com/api/zagadka/" + riddleId + "/getMiejsca";
        JsonListReceiver jsonListReceiver = new JsonListReceiver(this);
        jsonListReceiver.execute(apiUrl);
    }

    public void openProfileActivity() {
        Intent intent = new Intent(this, ProfilActivity.class);
        startActivity(intent);
        finish();
    }

    public void openMainPageActivity() {

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


}