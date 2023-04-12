package wro.per;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;


import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class Strona_glowna_Activity extends Activity {

	private MapView mapView;
	private Osm osm;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.strona_glowna_z_mapa);

		if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
		}
		else
			startService();


		final ImageButton button;

		button = (ImageButton) findViewById(R.id.listButton);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				System.out.println("działa");
				openActivity2();
			}
		});

		mapView = findViewById(R.id.map);
		osm = new Osm(mapView);

	}

	void startService() {
		LocationBroadcastReciever reciever = new LocationBroadcastReciever();
		IntentFilter filter = new IntentFilter("ACT_LOC");
		registerReceiver(reciever, filter);
		Intent intent = new Intent(Strona_glowna_Activity.this, LocationService.class);
		startService(intent);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case 1:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					startService();
				}
				else
					Toast.makeText(this, "Zaakceptuj uprawnienia do lokalizacji aby móc korzystać z aplikacji", Toast.LENGTH_LONG).show();
		}
	}

	public class LocationBroadcastReciever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals("ACT_LOC"))
			{
				double lat = intent.getDoubleExtra("latitude", 0f);
				double lon = intent.getDoubleExtra("longitude", 0f);
				Toast.makeText(Strona_glowna_Activity.this, "lat: " + lat + "  " +
						" lon: " + lon, Toast.LENGTH_LONG).show();
				System.out.println(lat + "    " + lon);

				osm.setPoint(new GeoPoint(lat,lon));
			}
		}
	}

	public void openActivity2(){
			Intent intent = new Intent(this, strona_zagadek.class);
			startActivity(intent);
		}
	}

	
	