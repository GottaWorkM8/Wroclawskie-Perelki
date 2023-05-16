package pl.wroc.projzesp.perelki.wrocperelki.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId  ;
    private String login ;
    private String password  ;
    private String email ;
    private Long points ;

    //@ManyToMany(mappedBy = "user_id")
    //private Set<Obiekt> found_Pieces ;

    //@ManyToMany(mappedBy = "user_id")
    //private Set<Riddle> completed_Riddles ;
}
