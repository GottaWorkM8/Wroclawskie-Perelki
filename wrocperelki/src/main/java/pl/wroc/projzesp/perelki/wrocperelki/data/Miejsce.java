package pl.wroc.projzesp.perelki.wrocperelki.data;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class Miejsce {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    private String trackingPosition;
    private String radius;
    private String photoLink;
    private String photoGps;
    private String telephonePosition;
    private String correctFoundLink;

    @ManyToOne
    @JoinColumn(name = "miejsca")
    private Zagadka zagadka;

    public Miejsce(String trackingPosition, String radius, String photoLink, String photoGps, String telephonePosition, String correctFoundLink, Zagadka zagadka) {
        this.trackingPosition = trackingPosition;
        this.radius = radius;
        this.photoLink = photoLink;
        this.photoGps = photoGps;
        this.telephonePosition = telephonePosition;
        this.correctFoundLink = correctFoundLink;
        this.zagadka = zagadka;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTrackingPosition() {
        return trackingPosition;
    }

    public void setTrackingPosition(String trackingPosition) {
        this.trackingPosition = trackingPosition;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public String getPhotoGps() {
        return photoGps;
    }

    public void setPhotoGps(String photoGps) {
        this.photoGps = photoGps;
    }

    public String getTelephonePosition() {
        return telephonePosition;
    }

    public void setTelephonePosition(String telephonePosition) {
        this.telephonePosition = telephonePosition;
    }

    public String getCorrectFoundLink() {
        return correctFoundLink;
    }

    public void setCorrectFoundLink(String correctFoundLink) {
        this.correctFoundLink = correctFoundLink;
    }

    public Zagadka getZagadka() {
        return zagadka;
    }

    public void setZagadka(Zagadka zagadka) {
        this.zagadka = zagadka;
    }
}
