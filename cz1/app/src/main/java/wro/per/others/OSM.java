package wro.per.others;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.GroundOverlay;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import wro.per.R;

public class OSM {

    private final IMapController mapController;
    private UserLocation userLocation;

    public OSM(MapView mapView) {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapController = mapView.getController();
        mapController.setZoom(18);
    }

    public void setPoint(GeoPoint point) {
        mapController.setCenter(point);
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

}
