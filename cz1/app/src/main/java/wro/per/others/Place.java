package wro.per.others;

import org.osmdroid.util.GeoPoint;

public class Place {

    public Place(GeoPoint location, boolean b){
        this.location = location;
        this.found = b;
    }

    private GeoPoint location;
    private GeoPoint detail;
    private GeoPoint observe;
    private double radius = 30;
    private boolean found;
    private String name;
    private String imageLink;
    private float azimuth;
    private float tilt;

    public GeoPoint getDetail() {
        return detail;
    }

    public GeoPoint getObserve() {
        return observe;
    }

    public String getName() {
        return name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public float getAzimuth() {
        return azimuth;
    }

    public float getTilt() {
        return tilt;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public boolean isFound() {
        return found;
    }

    public double getRadius() {
        return radius;
    }
}
