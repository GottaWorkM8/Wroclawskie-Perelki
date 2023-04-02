package pl.wroc.projzesp.perelki.wrocperelki.data;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
public class Pieces {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long piece_id ;
    private String name ;
    private String latitude ;
    private String longitude ;
    private String azimuth ;
    private String pitch ;
    private String roll ;
    private String verify_photo ;
    private String photo  ;
    private String website  ;
    private Long points  ;


    @OneToMany
    private Set<Riddle_Pieces> riddle_Pieces ;
    @OneToMany
    private Set<Found_Pieces> found_Pieces ;

    public Pieces(Long piece_id, String name, String latitude, String longitude, String azimuth, String pitch, String roll, String verify_photo, String photo, String website, Long points) {
        this.piece_id = piece_id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.azimuth = azimuth;
        this.pitch = pitch;
        this.roll = roll;
        this.verify_photo = verify_photo;
        this.photo = photo;
        this.website = website;
        this.points = points;
    }

    public Long getPiece_id() {
        return piece_id;
    }

    public void setPiece_id(Long piece_id) {
        this.piece_id = piece_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(String azimuth) {
        this.azimuth = azimuth;
    }

    public String getPitch() {
        return pitch;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getVerify_photo() {
        return verify_photo;
    }

    public void setVerify_photo(String verify_photo) {
        this.verify_photo = verify_photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public Set<Riddle_Pieces> getRiddle_Pieces() {
        return riddle_Pieces;
    }

    public void setRiddle_Pieces(Set<Riddle_Pieces> riddle_Pieces) {
        this.riddle_Pieces = riddle_Pieces;
    }

    public Set<Found_Pieces> getFound_Pieces() {
        return found_Pieces;
    }

    public void setFound_Pieces(Set<Found_Pieces> found_Pieces) {
        this.found_Pieces = found_Pieces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pieces pieces = (Pieces) o;
        return Objects.equals(piece_id, pieces.piece_id) && Objects.equals(name, pieces.name) && Objects.equals(latitude, pieces.latitude) && Objects.equals(longitude, pieces.longitude) && Objects.equals(azimuth, pieces.azimuth) && Objects.equals(pitch, pieces.pitch) && Objects.equals(roll, pieces.roll) && Objects.equals(verify_photo, pieces.verify_photo) && Objects.equals(photo, pieces.photo) && Objects.equals(website, pieces.website) && Objects.equals(points, pieces.points) && Objects.equals(riddle_Pieces, pieces.riddle_Pieces) && Objects.equals(found_Pieces, pieces.found_Pieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece_id, name, latitude, longitude, azimuth, pitch, roll, verify_photo, photo, website, points, riddle_Pieces, found_Pieces);
    }
}
