package pl.wroc.projzesp.perelki.wrocperelki.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wroc.projzesp.perelki.wrocperelki.model.LoggedUser;
import pl.wroc.projzesp.perelki.wrocperelki.model.User;

import java.util.List;

public interface LoggedUserRepository
        extends JpaRepository<LoggedUser, Long> {
    LoggedUser findByToken(String token);
    List<LoggedUser> findByUser(User user);

    void deleteByToken(String key);
}
