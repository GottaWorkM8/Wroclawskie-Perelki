package pl.wroc.projzesp.perelki.wrocperelki.data;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@Entity
public class Zagadka {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    private String category;
    private String title;
    private String description;
    private String congratulations;

    @OneToMany(mappedBy = "zagadka")
    private List<Miejsce> miejsca;

    public Zagadka(){
    }
    public Zagadka(String category, String title, String description, String congratulations){
        this.category = category;
        this.title = title;
        this.description = description;
        this.congratulations = congratulations;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCongratulations() {
        return congratulations;
    }

    public void setCongratulations(String congratulations) {
        this.congratulations = congratulations;
    }
}
