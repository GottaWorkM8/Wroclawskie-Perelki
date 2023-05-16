package pl.wroc.projzesp.perelki.wrocperelki.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Obiekt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;
    private String objectName ;
    private String objectPosition ;
    private String trackingPosition ;
    private String photoPosition ;
    private String photoShowRadius ;
    private String telephoneOrientation ;
    private String photoObjectLink  ;
    private String photoLink  ;
    private String infoLink  ;
    private boolean visible ;

    @ManyToOne
    private Riddle riddles ;



    private static final double EARTH_RADIUS = 6371000; // in m
    public static float calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;
        return (float)distance;
    }
    public float odlegloscDoPunktu(String gpsPosition) {
        String[] gps = gpsPosition.split(",");
        String[] gpsObiektu = objectPosition.split(",");
        //skrypt z internetu
        return calculateDistance(Float.parseFloat(gps[0]), Float.parseFloat(gps[1]), Float.parseFloat(gpsObiektu[0]), Float.parseFloat(gpsObiektu[1]));
    }
}
