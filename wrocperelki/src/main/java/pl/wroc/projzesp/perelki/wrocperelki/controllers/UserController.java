package pl.wroc.projzesp.perelki.wrocperelki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.LoggedUserRepository;
import pl.wroc.projzesp.perelki.wrocperelki.model.LoggedUser;
import pl.wroc.projzesp.perelki.wrocperelki.model.User;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.UserRepository;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.hash;

@RestController
public class UserController {

    @Autowired
    private UserRepository users;
    @Autowired
    private LoggedUserRepository loggedUsers;

    @PostMapping("/api/user/testlogin")
    boolean testLogin(@RequestBody Map<String,String> map) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if(map.isEmpty())return false;
        if(!map.containsKey("password"))
            return false;

        User u = null;
        if(map.containsKey("login"))
            u = users.findByLogin(map.get("login"));
        else if(map.containsKey("email"))
            u = users.findByEmail(map.get("email"));

        if(u!=null) {
            map.put("login",u.getLogin());
            return Objects.equals(u.getPassword(), passhash(map.get("password")));
        }
        return false;
    }

    private String passhash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = new byte[16];
        for (int i = 0; i < 16; i++) {
            salt[i] = (byte) (i+33);
        }
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return Arrays.toString(hash);
    }

    @PostMapping("/api/user/login")
    String login(@RequestBody Map<String,String> map) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if(testLogin(map)) {
            String token = RandomString(20);
            loggedUsers.save(new LoggedUser(token,users.findByLogin(map.get("login"))));
            return token;
        }
        return null;
    }

    @GetMapping("/api/user/testkey")
    boolean testkey(@RequestParam String key) {
        if(loggedUsers.findByToken(key)!=null) {
            return true;
        }
        return false;
    }

    @GetMapping("/api/user/logout")
    boolean logout(@RequestParam String key) {
        LoggedUser lu = loggedUsers.findByToken(key);
        if(lu!=null) {
            loggedUsers.delete(lu);
            return true;
        }
        return false;
    }

    private String RandomString(int i) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(i);
        for (int j = 0; j < i; j++) {
            sb.append(AB.charAt((int)(Math.random() * AB.length())));
        }
        return sb.toString();
    }

    @PostMapping("/api/user/register")
    boolean register(@RequestBody Map<String,String> map) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if(map.isEmpty())return false;
        if(!map.containsKey("login"))return false;
        if(!map.containsKey("password"))return false;
        if(!map.containsKey("email"))return false;
        if(users.findByLogin(map.get("login"))!=null)return false;
        if(users.findByEmail(map.get("email"))!=null)return false;
        if(map.get("password").length()<3)return false;
        if(map.get("login").length()<3)return false;
        if(map.get("email").length()<3)return false;
        if(!map.get("email").contains("@"))return false;
        if(!map.get("email").contains("."))return false;
        users.save(new User(0L,map.get("login"),passhash(map.get("password")),map.get("email"),0L));
        return true;
    }


    @GetMapping("/api/user/all")
    Iterable<User> getAll() {
        return users.findAll();
    }
    @GetMapping("/api/user/{id}")
    User getUser(@PathVariable Long id) throws Exception {
        //todo identification
        return users.findById(id)
                .orElseThrow(() -> new Exception(String.valueOf(id)));
    }

    @DeleteMapping("/api/user/{id}")
    void deleteUser(@PathVariable Long id) {
        users.deleteById(id);
    }

}
