package pl.wroc.projzesp.perelki.wrocperelki.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wroc.projzesp.perelki.wrocperelki.model.Obiekt;

import java.util.List;

public interface ObiektRepository
        extends JpaRepository<Obiekt, Long> {
    List<Obiekt> findAll();
}
