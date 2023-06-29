package wro.per.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;


import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import wro.per.BuildConfig;
import wro.per.others.LocationService;
import wro.per.others.OSM;
import wro.per.R;
import wro.per.others.Place;
import wro.per.others.Places;
import wro.per.others.Riddles;

public class MainPageActivity extends AppCompatActivity {

    private TextView textHint;
    private Places places;
    private static MapView mapView;
    private OSM osm;

    private double latitude;

    private double longitude;

    private boolean isActivityStarted = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_layout);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else
            startService();

        final ImageButton openRiddlesMenuButton;

        openRiddlesMenuButton = (ImageButton) findViewById(R.id.listButton);
        openRiddlesMenuButton.setOnClickListener(view -> openSolvedActivity());


        final ImageButton openFavouritesMenuButton;

        openFavouritesMenuButton = findViewById(R.id.favouriteButton);
        openFavouritesMenuButton.setOnClickListener(view -> openFavouritesActivity());

        final ImageButton openProfileMenuButton;

        openProfileMenuButton = (ImageButton) findViewById(R.id.profileButton);
        openProfileMenuButton.setOnClickListener(view -> openProfileActivity());

        final ImageButton openInfoMenuButton;

        openInfoMenuButton = (ImageButton) findViewById(R.id.settingsButton);
        openInfoMenuButton.setOnClickListener(view -> openInfoActivity());

        ImageButton locateButton = findViewById(R.id.locateMeButton);
        locateButton.setOnClickListener(view -> osm.setPoint(showLocation()));

        ImageButton questionMarkButton = findViewById(R.id.questionMarkButton);
        textHint = findViewById(R.id.textHint);
        textHint.setTextColor(getResources().getColor(R.color.yellow));
        textHint.setGravity(Gravity.CENTER);

        questionMarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textHint.getVisibility() == View.VISIBLE) {
                    textHint.setVisibility(View.GONE);
                } else {
                    textHint.setVisibility(View.VISIBLE);
                }
            }
        });


        mapView = findViewById(R.id.map);
        osm = new OSM(mapView);
        places = Places.getInstance();
        places.setMapView(mapView);

        //ArrayList<Place> wroclawGeoPoints = new ArrayList<>();
        //wroclawGeoPoints.add(new Place(new GeoPoint(51.10884908275765, 17.060501791763073), true, null));
        //wroclawGeoPoints.add(new Place(new GeoPoint(51.10912762329181, 17.05943358599828), true, null));
        //wroclawGeoPoints.add(new Place(new GeoPoint(51.10462007876176, 16.944762273337567), true, null));
        //places.setPlaces(wroclawGeoPoints);
        //places.drawPlaces();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        checkDistance();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private void checkDistance(){
        Place nearbyPlace = places.isClose(showLocation());

        if(nearbyPlace != null && !isActivityStarted &&  nearbyPlace.isFound() == false)
        {
            isActivityStarted = true;
            Intent intent = new Intent(this, FoundObjectActivity.class);
            intent.putExtra("nearbyPlace", nearbyPlace.getObject().toString());  // Przekazanie miejsca do Intent jako dodatkowe dane
            startActivity(intent);
            nearbyPlace.setFound(true);
        }
    }

    void startService() {
        LocationBroadcastReciever reciever = new LocationBroadcastReciever();
        IntentFilter filter = new IntentFilter("ACT_LOC");
        registerReceiver(reciever, filter);
        Intent intent = new Intent(MainPageActivity.this, LocationService.class);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startService();
                } else
                    Toast.makeText(this, "Zaakceptuj uprawnienia do lokalizacji aby móc korzystać z aplikacji", Toast.LENGTH_LONG).show();
        }
    }

    public class LocationBroadcastReciever extends BroadcastReceiver {

        private boolean isPointSet = false;

        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            places.setMapView(mapView);
            if (intent.getAction().equals("ACT_LOC")) {
                latitude = intent.getDoubleExtra("latitude", 0f);
                longitude = intent.getDoubleExtra("longitude", 0f);

                GeoPoint point = new GeoPoint(latitude, longitude);
                if (!isPointSet) {
                    osm.setPoint(point);
                    isPointSet = true;
                }

                osm.drawYou(mapView, point);
                places.drawRing(mapView, point);
                textHint.setText("min: " + places.close(point) + " m\n max: " + places.far(point) + " m");
            }
        }
    }

    private GeoPoint showLocation() {
        return new GeoPoint(latitude,longitude);
    }

    public void openSolvedActivity() {
        Intent intent = new Intent(this, SolvedActivity.class);
        startActivity(intent);
    }

    public void openFavouritesActivity() {
        Intent intent = new Intent(this, FavouritesActivity.class);
        startActivity(intent);
    }

    public void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void openInfoActivity() {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    public void openFoundObjectActivity() {
        Intent intent = new Intent(this, FoundObjectActivity.class);
        startActivity(intent);
    }
}



	
	