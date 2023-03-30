package pl.wroc.projzesp.perelki.wrocperelki.interfaces;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.wroc.projzesp.perelki.wrocperelki.data.Riddles;

import java.util.List;


public interface RiddlesRepository
        extends JpaRepository<Riddles, Long> {
    @NotNull List<Riddles> findAll();
}
