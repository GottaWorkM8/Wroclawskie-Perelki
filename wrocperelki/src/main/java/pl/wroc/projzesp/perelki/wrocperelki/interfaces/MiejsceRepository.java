package pl.wroc.projzesp.perelki.wrocperelki.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wroc.projzesp.perelki.wrocperelki.data.Miejsce;

import java.util.List;

public interface MiejsceRepository
        extends JpaRepository<Miejsce, Long> {
    List<Miejsce> findAll();
}
