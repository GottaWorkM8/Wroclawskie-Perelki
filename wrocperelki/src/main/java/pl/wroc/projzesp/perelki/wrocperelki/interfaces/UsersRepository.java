package pl.wroc.projzesp.perelki.wrocperelki.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wroc.projzesp.perelki.wrocperelki.data.Riddles;
import pl.wroc.projzesp.perelki.wrocperelki.data.Users;

public interface UsersRepository
        extends JpaRepository<Users, Long> {
}
