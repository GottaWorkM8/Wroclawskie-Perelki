package pl.wroc.projzesp.perelki.wrocperelki.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wroc.projzesp.perelki.wrocperelki.model.LoggedUser;
import pl.wroc.projzesp.perelki.wrocperelki.model.User;

public interface LoggedUserRepository
        extends JpaRepository<LoggedUser, Long> {
    LoggedUser findByToken(String token);
    LoggedUser findByUser(User user);
}
