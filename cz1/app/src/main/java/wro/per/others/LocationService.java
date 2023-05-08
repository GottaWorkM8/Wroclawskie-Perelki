package wro.per.others;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationService extends Service {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    public void onCreate() {
        super.onCreate();

        // Inicjalizacja klienta FusedLocationProvider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Utworzenie obiektu LocationRequest i ustawienie parametrów aktualizacji lokalizacji
        locationRequest = new LocationRequest();
        locationRequest.setInterval(500); // 0.5 sekundy
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Utworzenie obiektu LocationCallback do obsługi aktualizacji lokalizacji
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                // Wysłanie nowych danych o lokalizacji do aktywności przez Broadcast
                Intent intent = new Intent("ACT_LOC");
                intent.putExtra("latitude", locationResult.getLastLocation().getLatitude());
                intent.putExtra("longitude", locationResult.getLastLocation().getLongitude());
                sendBroadcast(intent);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Wywołanie metody do żądania aktualizacji lokalizacji
        requestLocationUpdates();

        // Powiadomienie systemu, że usługa nie powinna być zatrzymywana w przypadku braku zasobów
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Wywołanie metody do zatrzymania aktualizacji lokalizacji
        removeLocationUpdates();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void requestLocationUpdates() {
        // Sprawdzenie uprawnień do lokalizacji
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Wywołanie metody do żądania aktualizacji lokalizacji
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void removeLocationUpdates() {
        // Wywołanie metody do zatrzymania aktualizacji lokalizacji
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}

