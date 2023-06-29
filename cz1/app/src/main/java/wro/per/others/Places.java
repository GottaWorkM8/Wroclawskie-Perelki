package wro.per.others;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Places {
    private static Places instance;

    private ArrayList<Place> places;

    private ArrayList<PlaceImage> drawnPlaces;

    private CircleImage cmin, cmax;

    private MapView mapView;

    public ArrayList<Place> getPlaces() {
        return places;
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    public Places(){
        places = new ArrayList<Place>();
        drawnPlaces = new ArrayList<PlaceImage>();
    }

    public void drawPlaces(){
        int i = 0;
        for (Place p : places){
            if(p.isFound()){
                drawnPlaces.add(new PlaceImage(p.getLocation()));
            }
        }
        for (PlaceImage pi : drawnPlaces){
            mapView.getOverlays().add(pi);
        }
    }

    public void deletePlaces(){
        for (PlaceImage p : drawnPlaces){
            mapView.getOverlays().remove(p);
        }
        places.clear();
        drawnPlaces.clear();
    }

    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
    }

    public Place isClose(GeoPoint geoPoint){

        for(Place p : places){
            System.out.println(p.getLocation().distanceToAsDouble(geoPoint) + "   " +  p.getRadius());
            if(p.getLocation().distanceToAsDouble(geoPoint) < p.getRadius()){
                return p;
            }
        }
        return null;
    }

    public void drawRing(MapView mapView, GeoPoint geoPoint){
        deleteRing(mapView);
        if(!places.isEmpty()){
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            for (Place place : places) {
                GeoPoint placePoint = place.getLocation();
                double distance = geoPoint.distanceToAsDouble(placePoint);

                if (distance < min) {
                    min = distance;
                }

                if (distance > max) {
                    max = distance;
                }
            }

            cmin = new CircleImage(geoPoint, mapView, (float) min);

            cmax = new CircleImage(geoPoint, mapView, (float) max);
            mapView.getOverlays().add(cmax);
            mapView.getOverlays().add(cmin);
        }
    }

    public void deleteRing(MapView mapView) {
        mapView.getOverlays().remove(cmin);
        mapView.getOverlays().remove(cmax);
    }

    public String close(GeoPoint geoPoint){
        if(!places.isEmpty()) {
            double min = Double.MAX_VALUE;
            for (Place place : places) {
                GeoPoint placePoint = place.getLocation();
                double distance = geoPoint.distanceToAsDouble(placePoint);

                if (distance < min) {
                    min = distance;
                }
            }
            min /= 1000;
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            decimalFormat.setRoundingMode(RoundingMode.DOWN);
            return decimalFormat.format(min);
        }
        return "-";
    }

    public String far(GeoPoint geoPoint){
        if(!places.isEmpty()) {
            double max = Double.MIN_VALUE;
            for (Place place : places) {
                GeoPoint placePoint = place.getLocation();
                double distance = geoPoint.distanceToAsDouble(placePoint);
                if (distance > max) {
                    max = distance;
                }
            }
            max /= 1000;
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            decimalFormat.setRoundingMode(RoundingMode.DOWN);
            return decimalFormat.format(max);
        }
        return "-";
    }

    public static synchronized Places getInstance() {
        if (instance == null) {
            instance = new Places();
        }
        return instance;
    }
}
