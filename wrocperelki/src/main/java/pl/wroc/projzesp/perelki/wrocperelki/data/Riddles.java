package pl.wroc.projzesp.perelki.wrocperelki.data;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;


@Entity
public class Riddles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long riddle_id;
    private String name;
    private String category;
    private String info;
    private String congrats;
    private Long points;

    @OneToMany
    private Set<Riddle_Pieces> riddle_Pieces ;
    @OneToMany
    private Set<Completed_Riddles> completed_Riddles ;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Riddles riddles = (Riddles) o;
        return Objects.equals(riddle_id, riddles.riddle_id) && Objects.equals(name, riddles.name) && Objects.equals(category, riddles.category) && Objects.equals(info, riddles.info) && Objects.equals(congrats, riddles.congrats) && Objects.equals(points, riddles.points) && Objects.equals(riddle_Pieces, riddles.riddle_Pieces) && Objects.equals(completed_Riddles, riddles.completed_Riddles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(riddle_id, name, category, info, congrats, points, riddle_Pieces, completed_Riddles);
    }

    public Long getRiddle_id() {
        return riddle_id;
    }

    public void setRiddle_id(Long riddle_id) {
        this.riddle_id = riddle_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCongrats() {
        return congrats;
    }

    public void setCongrats(String congrats) {
        this.congrats = congrats;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public Riddles(Long riddle_id, String name, String category, String info, String congrats, Long points) {
        this.riddle_id = riddle_id;
        this.name = name;
        this.category = category;
        this.info = info;
        this.congrats = congrats;
        this.points = points;
    }

    public Riddles() {
    }


}
