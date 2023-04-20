package pl.wroc.projzesp.perelki.wrocperelki.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String phone ;
    private String access ;
    private Long points ;
    //@OneToMany(mappedBy = "user_id")
    //private Set<Found_Pieces> found_Pieces ;
    //@OneToMany(mappedBy = "user_id")
    //private Set<Completed_Riddles> completed_Riddles ;
}
