package pl.wroc.projzesp.perelki.wrocperelki.interfaces;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.wroc.projzesp.perelki.wrocperelki.data.Zagadka;

import java.util.List;


public interface ZagadkaRepository
        extends JpaRepository<Zagadka, Long> {
    @NotNull List<Zagadka> findAll();
}
