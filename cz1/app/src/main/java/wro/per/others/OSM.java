package wro.per.others;


import static java.security.AccessController.getContext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.GroundOverlay;

import wro.per.R;

public class OSM {
    private IMapController mapController;
    public OSM(MapView mapView){
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapController = mapView.getController();
        mapController.setZoom(18);
    }


    public void setPoint(GeoPoint point){
        mapController.setCenter(point);
    }

    public void draw(Context ctx, MapView mapView, GeoPoint tlpoint, GeoPoint brpoint){
        Bitmap icon = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.czarna_ikona_gwiazdy);
        GroundOverlay groundOverlay = new GroundOverlay();
        groundOverlay.setImage(icon);
        groundOverlay.setPosition(tlpoint, brpoint);
        mapView.getOverlayManager().add(groundOverlay);
    }

    
}
