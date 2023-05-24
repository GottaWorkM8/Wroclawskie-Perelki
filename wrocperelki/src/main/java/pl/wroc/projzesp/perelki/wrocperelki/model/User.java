package pl.wroc.projzesp.perelki.wrocperelki.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private boolean admin;
    private Long points ;

    @JsonIgnore
    @ManyToMany
    private Set<Obiekt> znalezioneMiejsca;

    @JsonIgnore
    @ManyToMany
    private Set<Riddle> znalezioneZagadki;

    @JsonIgnore
    @ManyToMany
    private Set<Obiekt> ulubioneMiejsca;
}
