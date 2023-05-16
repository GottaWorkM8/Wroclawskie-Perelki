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
public class LoggedUser {
    @Id
    private String token;
    @ManyToOne
    private User user;
}
