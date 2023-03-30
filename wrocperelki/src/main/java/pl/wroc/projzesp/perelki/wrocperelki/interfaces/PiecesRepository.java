package pl.wroc.projzesp.perelki.wrocperelki.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wroc.projzesp.perelki.wrocperelki.data.Pieces;

import java.util.List;

public interface PiecesRepository
        extends JpaRepository<Pieces, Long> {
    List<Pieces> findAll();
}
