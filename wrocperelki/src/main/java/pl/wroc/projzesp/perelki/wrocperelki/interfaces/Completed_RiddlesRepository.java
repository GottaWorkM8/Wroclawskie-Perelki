package pl.wroc.projzesp.perelki.wrocperelki.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wroc.projzesp.perelki.wrocperelki.data.Completed_Riddles;

public interface Completed_RiddlesRepository
        extends JpaRepository<Completed_Riddles, Long> {
}
