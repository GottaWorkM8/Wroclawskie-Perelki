package wro.per.others;

import org.osmdroid.util.GeoPoint;

public class Place {
    private GeoPoint location;
    private boolean found;
    private double distance;

    public Place(GeoPoint location, boolean b){
        this.location = location;
        this.found = b;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public boolean isFound() {
        return found;
    }

    public double getDistance() {
        return distance;
    }
}
