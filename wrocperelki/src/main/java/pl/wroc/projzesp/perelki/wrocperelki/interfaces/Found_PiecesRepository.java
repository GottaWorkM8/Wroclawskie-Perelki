package pl.wroc.projzesp.perelki.wrocperelki.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wroc.projzesp.perelki.wrocperelki.data.Found_Pieces;

public interface Found_PiecesRepository
        extends JpaRepository<Found_Pieces, Long> {
}
