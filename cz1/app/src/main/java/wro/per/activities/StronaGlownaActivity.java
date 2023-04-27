package wro.per.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;


import android.view.View;
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

public class StronaGlownaActivity extends AppCompatActivity {

    private MapView mapView;
    private OSM osm;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.strona_glowna_z_mapa_layout);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else
            startService();

        final ImageButton otworzMenuZadaniaButton;

        otworzMenuZadaniaButton = (ImageButton) findViewById(R.id.listButton);
        otworzMenuZadaniaButton.setOnClickListener(view -> otworzRozwiazaneActivity());

        final ImageButton otworzMenuUlubioneButton;

        otworzMenuUlubioneButton = (ImageButton) findViewById(R.id.favouriteButton);
        otworzMenuUlubioneButton.setOnClickListener(view -> otworzUlubioneActivity());

        final ImageButton otworzMenuProfilButton;

        otworzMenuProfilButton = (ImageButton) findViewById(R.id.profileButton);
        otworzMenuProfilButton.setOnClickListener(view -> otworzProfilActivity());

        final ImageButton otworzMenuInfoButton;

        otworzMenuInfoButton = (ImageButton) findViewById(R.id.settingsButton);
        otworzMenuInfoButton.setOnClickListener(view -> otworzKalibracjaActivity());

        mapView = findViewById(R.id.map);
        osm = new OSM(mapView);

    }

    void startService() {
        LocationBroadcastReciever reciever = new LocationBroadcastReciever();
        IntentFilter filter = new IntentFilter("ACT_LOC");
        registerReceiver(reciever, filter);
        Intent intent = new Intent(StronaGlownaActivity.this, LocationService.class);
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
            if (intent.getAction().equals("ACT_LOC")) {
                double lat = intent.getDoubleExtra("latitude", 0f);
                double lon = intent.getDoubleExtra("longitude", 0f);

                System.out.println(lat + "    " + lon);

                osm.setPoint(new GeoPoint(lat, lon));
                osm.draw(context, mapView, new GeoPoint(lat - 0.0005, lon - 0.0005), new GeoPoint(lat + 0.0005, lon + 0.0005));
            }
        }
    }

    public void otworzRozwiazaneActivity() {
        Intent intent = new Intent(this, RozwiazaneZagadkiActivity.class);
        startActivity(intent);
        
    }

    public void otworzUlubioneActivity() {
        Intent intent = new Intent(this, UlubioneActivity.class);
        startActivity(intent);
    }

    public void otworzProfilActivity() {
        Intent intent = new Intent(this, ProfilActivity.class);
        startActivity(intent);
    }

    public void otworzKalibracjaActivity() {
        Intent intent = new Intent(this, KalibracjaActivity.class);
        startActivity(intent);
    }
}



	
	