package pl.wroc.projzesp.perelki.wrocperelki.data;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    @JsonIgnore
    @OneToMany
    private Set<Riddle_Pieces> riddle_Pieces ;
    @JsonIgnore
    @OneToMany
    private Set<Completed_Riddles> completed_Riddles ;
}
