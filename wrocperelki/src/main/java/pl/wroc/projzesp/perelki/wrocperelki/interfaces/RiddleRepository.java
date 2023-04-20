package pl.wroc.projzesp.perelki.wrocperelki.interfaces;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.wroc.projzesp.perelki.wrocperelki.model.Riddle;

import java.util.List;


public interface RiddleRepository
        extends JpaRepository<Riddle, Long> {
    @NotNull List<Riddle> findAll();
}
