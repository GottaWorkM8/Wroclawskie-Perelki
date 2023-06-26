package pl.wroc.projzesp.perelki.wrocperelki.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.wroc.projzesp.perelki.wrocperelki.model.LoggedUser;
import pl.wroc.projzesp.perelki.wrocperelki.model.User;

import java.util.List;

public interface LoggedUserRepository
        extends JpaRepository<LoggedUser, String> {
    LoggedUser findByToken(String token);
    List<LoggedUser> findByUser(User user);
    @Transactional
    void deleteByToken(String key);
    @Transactional
    void removeByToken(String key);
}
