package pl.wroc.projzesp.perelki.wrocperelki.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Riddle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String difficulty;
    private String name;
    private int objectCount;
    private String infolink;
    private String author;
    private int points;
    private boolean visible ;

    @JsonIgnore
    @OneToMany
    private Set<Obiekt> obiekty ;

    //@JsonIgnore
    //@OneToMany
    //private Set<Completed_Riddles> completed_Riddles ;
}
