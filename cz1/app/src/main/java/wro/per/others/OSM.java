package wro.per.others;


import android.annotation.SuppressLint;
import android.view.MotionEvent;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

public class OSM {

    private final IMapController mapController;
    private UserLocationImage userLocation;

    private GeoPoint geoPoint;

    private boolean track;
    private long lastTouchTime = 0;

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
        if(geoPoint != null){
            deleteYou(mapView);
            userLocation = new UserLocationImage(geoPoint);
            mapView.getOverlays().add(userLocation);
        }

    }



    public void deleteYou(MapView mapView) {
        mapView.getOverlays().remove(userLocation);
    }
}
