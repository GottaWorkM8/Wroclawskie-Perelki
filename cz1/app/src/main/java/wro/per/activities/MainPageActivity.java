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

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;


import wro.per.BuildConfig;
import wro.per.others.LocationService;
import wro.per.others.OSM;
import wro.per.R;

public class MainPageActivity extends AppCompatActivity {

    private MapView mapView;
    private OSM osm;

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
        openFavouritesMenuButton.setOnClickListener(view -> openFavouritesActivity());

        final ImageButton openProfileMenuButton;

        openProfileMenuButton = (ImageButton) findViewById(R.id.profileButton);
        openProfileMenuButton.setOnClickListener(view -> openProfileActivity());

        final ImageButton openInfoMenuButton;

        openInfoMenuButton = (ImageButton) findViewById(R.id.settingsButton);
        openInfoMenuButton.setOnClickListener(view -> openCalibrationActivity());

        mapView = findViewById(R.id.map);
        osm = new OSM(mapView);

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
            if (intent.getAction().equals("ACT_LOC")) {
                double lat = intent.getDoubleExtra("latitude", 0f);
                double lon = intent.getDoubleExtra("longitude", 0f);

                System.out.println(lat + "    " + lon);
                GeoPoint point = new GeoPoint(lat, lon);
                osm.setPoint(point);
                osm.drawYou(mapView, point);
                //osm.drawCircle(mapView, point, 100);
                //osm.drawCircle(mapView, point, 150);
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
        Intent intent = new Intent(this, EditObjectActivity.class);
        startActivity(intent);
    }

    public void openCalibrationActivity() {
        Intent intent = new Intent(this, CalibrationActivity.class);
        intent.putExtra("otwarcieAplikacji", false);
        startActivity(intent);
    }
}



	
	