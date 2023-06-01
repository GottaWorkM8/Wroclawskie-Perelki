package wro.per.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;


import android.widget.ImageButton;
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
import wro.per.others.Places;
import wro.per.others.Riddles;

public class MainPageActivity extends AppCompatActivity {

    private Places places;
    private MapView mapView;
    private OSM osm;
    public static ArrayList<Riddles> riddlesArrayList;
    public HashMap<Integer, String> objectHashMap = new HashMap<>();

    public static ArrayList<HashMap<Integer,String>> objectsArrayList = new ArrayList<>();

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

        openFavouritesMenuButton = (ImageButton) findViewById(R.id.favouriteButton);


        final ImageButton openProfileMenuButton;

        openProfileMenuButton = (ImageButton) findViewById(R.id.profileButton);
        openProfileMenuButton.setOnClickListener(view -> openProfileActivity());

        final ImageButton openInfoMenuButton;

        openInfoMenuButton = (ImageButton) findViewById(R.id.settingsButton);
        openInfoMenuButton.setOnClickListener(view -> openInfoActivity());

        mapView = findViewById(R.id.map);
        osm = new OSM(mapView);
        places = new Places(mapView);

        ArrayList<GeoPoint> americaGeoPoints = new ArrayList<>();

        americaGeoPoints.add(new GeoPoint(37.7749, -122.4194)); // San Francisco, California
        americaGeoPoints.add(new GeoPoint(34.0522, -118.2437)); // Los Angeles, California
        americaGeoPoints.add(new GeoPoint(40.7128, -74.0060));  // New York City, New York
        americaGeoPoints.add(new GeoPoint(41.8781, -87.6298));  // Chicago, Illinois
        americaGeoPoints.add(new GeoPoint(29.7604, -95.3698));  // Houston, Texas
        americaGeoPoints.add(new GeoPoint(25.7617, -80.1918));  // Miami, Florida
        americaGeoPoints.add(new GeoPoint(39.9526, -75.1652));  // Philadelphia, Pennsylvania
        americaGeoPoints.add(new GeoPoint(33.4484, -112.0740)); // Phoenix, Arizona
        americaGeoPoints.add(new GeoPoint(32.7767, -96.7970));  // Dallas, Texas
        americaGeoPoints.add(new GeoPoint(47.6062, -122.3321));

        class FetchData extends Thread {

            public ArrayList<Riddles> riddlesArrList = new ArrayList<>();

            @Override
            public void run() {
                try {
                    URL url = new URL("https://szajsjem.mooo.com/api/zagadka");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    StringBuilder data = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        data.append(line);
                    }

                    JSONArray jsonArray = new JSONArray(data.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Riddles riddle = new Riddles();
                        riddle.setId(jsonObject.getInt("id"));
                        riddle.setDifficulty(jsonObject.getString("difficulty"));
                        riddle.setName(jsonObject.getString("name"));
                        riddle.setObjectCount(jsonObject.isNull("objectCount") ? null : jsonObject.getInt("objectCount"));
                        riddle.setInfoLink(jsonObject.getString("infolink"));
                        riddle.setAuthor(jsonObject.getString("author"));
                        riddle.setPoints(jsonObject.isNull("points") ? null : jsonObject.getInt("points"));
                        riddlesArrList.add(riddle);
                    }
                    bufferedReader.close();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        class FetchDataObject extends Thread {
            @Override
            public void run() {
                try {
                    for(Riddles riddle : riddlesArrayList){
                        URL url = new URL("https://szajsjem.mooo.com/api/zagadka/" + riddle.getId() + "/getMiejsca");
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;

                        StringBuilder data = new StringBuilder();
                        while ((line = bufferedReader.readLine()) != null) {
                            data.append(line);
                        }

                        JSONArray jsonArray = new JSONArray(data.toString());
                        objectsArrayList.add(new HashMap<>());
                        System.out.println(objectsArrayList.size());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            objectsArrayList.get(objectsArrayList.size()-1).put(jsonObject.getInt("id"), jsonObject.getString("objectName"));
                            System.out.println(objectHashMap.get("id"));
                        }

                        bufferedReader.close();
                    }
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }


        FetchData fetchData = new FetchData();
        fetchData.start(); // uruchamia wątek i wywołuje metodę run()

        while (fetchData.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        riddlesArrayList = fetchData.riddlesArrList;

        FetchDataObject fetchDataObject = new FetchDataObject();
        fetchDataObject.start(); // uruchamia wątek i wywołuje metodę run()
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

        @Override
        public void onReceive(Context context, Intent intent) {
            osm.deleteYou(mapView);
            places.deleteRing(mapView);
            if (intent.getAction().equals("ACT_LOC")) {
                double lat = intent.getDoubleExtra("latitude", 0f);
                double lon = intent.getDoubleExtra("longitude", 0f);

                System.out.println(lat + "    " + lon);
                GeoPoint point = new GeoPoint(lat, lon);
                osm.setPoint(point);
                osm.drawYou(mapView, point);
                places.drawRing(mapView, point);
            }
        }
    }

    public void openSolvedActivity() {
        Intent intent = new Intent(this, SolvedActivity.class);
        startActivity(intent);
        
    }

    public void openFavouritesActivity() {
        Intent intent = new Intent(this, SensorsActivity.class);
        startActivity(intent);
    }

    public void openProfileActivity() {
        Intent intent = new Intent(this, ProfilActivity.class);
        startActivity(intent);
    }

    public void openInfoActivity() {
        Intent intent = new Intent(this, InfoActivity.class);

        startActivity(intent);
    }
}



	
	