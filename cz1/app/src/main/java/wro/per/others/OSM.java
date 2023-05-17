package wro.per.others;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.google.android.gms.maps.model.GroundOverlay;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

import wro.per.R;

public class OSM {

    private final IMapController mapController;
    private UserLocation userLocation;

    private GeoPoint geoPoint;

    private boolean track;
    private long lastTouchTime = 0;
    private ArrayList<Place> places;

    @SuppressLint("ClickableViewAccessibility")
    public OSM(MapView mapView) {
        track = true;
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapController = mapView.getController();
        mapController.setZoom(18);

        mapView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - lastTouchTime;
                lastTouchTime = currentTime;

                if (elapsedTime <= 300) {
                    track = !track;
                    return true;
                }
            }
            return false;
        });
    }

    public void setPoint(GeoPoint point) {
        geoPoint = point;
        if (track){
            mapController.setCenter(geoPoint);
        }
    }

    public void drawYou(MapView mapView, GeoPoint geoPoint) {
        userLocation = new UserLocation(geoPoint);
        mapView.getOverlays().add(userLocation);
    }

    public void drawCircle(MapView mapView, GeoPoint geoPoint, float r) {
        mapView.getOverlays().add(new Circle(geoPoint, mapView, r));
    }

    public void deleteYou(MapView mapView) {
        mapView.getOverlays().remove(userLocation);
    }

    public void drawPlaces(MapView mapView, ArrayList<GeoPoint> points){
        places = new ArrayList<>();
        int i = 0;
        for (GeoPoint geoPoint : points){
            places.add(new Place(geoPoint));
            mapView.getOverlays().add(places.get(i));
            i++;
        }
    }

    public void deletePlaces(MapView mapView){
        for (Place place : places){
            mapView.getOverlays().remove(place);
        }
    }

}
