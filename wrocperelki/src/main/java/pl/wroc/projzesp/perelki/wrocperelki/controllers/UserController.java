package pl.wroc.projzesp.perelki.wrocperelki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.LoggedUserRepository;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.ObiektRepository;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.RiddleRepository;
import pl.wroc.projzesp.perelki.wrocperelki.model.LoggedUser;
import pl.wroc.projzesp.perelki.wrocperelki.model.Obiekt;
import pl.wroc.projzesp.perelki.wrocperelki.model.Riddle;
import pl.wroc.projzesp.perelki.wrocperelki.model.User;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.UserRepository;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;

import static java.util.Objects.hash;

@RestController
public class UserController {

    @Autowired
    private UserRepository users;
    @Autowired
    private LoggedUserRepository loggedUsers;
    @Autowired
    private ObiektRepository miejsca;
    @Autowired
    private RiddleRepository riddles;

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

    @PostMapping("/api/user/login")
    String login(@RequestBody Map<String,String> map) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if(testLogin(map)) {
            String token = RandomString(20);
            while(loggedUsers.findByToken(token)!=null) {
                token = RandomString(20);
            }
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
        users.save(new User(0L,map.get("login"),passhash(map.get("password")),map.get("email"),false,0L,new HashSet<>(),new HashSet<>(),new HashSet<>()));
        return true;
    }


    @GetMapping("/api/user/all")
    Iterable<User> getAll(@RequestParam String key) {
        if(isKeyAdminLogged(key))
            return users.findAll();
        return null;
    }
    @GetMapping("/api/user")
    User getUser(@RequestParam String key) throws Exception {
        User u = getUserByKey(key);
        if(u==null)throw new Exception("Not logged");
        return u;
    }
    @GetMapping("/api/user/logout")
    void forceLogout(@RequestParam String key) {
        if(loggedUsers.findByToken(key)!=null)
            loggedUsers.deleteByToken(key);
    }
    @DeleteMapping("/api/user/logoutAll")
    void deleteAll(@RequestParam String key) {
        if(isKeyAdminLogged(key))
                loggedUsers.deleteAll();
    }
    @DeleteMapping("/api/user/{id}")
    void deleteUser(@PathVariable Long id,@RequestParam String key) {
        User u = getUserByKey(key);
        if(u==null)return;
        if(Objects.equals(u.getUserId(), id)) {
            loggedUsers.deleteByToken(key);
            users.deleteById(id);
        }
    }

    @GetMapping("/api/user/makeAdmin/{id}")
    void makeAdmin(@PathVariable Long id,@RequestParam String key) {
        if(isKeyAdminLogged(key)) {
            users.findById(id).ifPresent(user -> user.setAdmin(true));
        }
    }

    @GetMapping("/api/user/ulubioneMiejsca")
    Iterable<Obiekt> getUlubioneMiejsca(@RequestParam String key) {
        User u = getUserByKey(key);
        if(u==null)return null;
        return u.getUlubioneMiejsca();
    }
    @PostMapping("/api/user/ulubioneMiejsca")
    void addUlubioneMiejsca(@RequestParam String key,@RequestBody Map<String,String> map) {
        User u = getUserByKey(key);
        if(u==null)return;
        if(map.containsKey("id")) {
            Optional<Obiekt> o = miejsca.findById(Long.parseLong(map.get("id")));
            if(o.isEmpty())return;
            if(u.getUlubioneMiejsca().contains(o.get()))
                u.getUlubioneMiejsca().remove(o.get());
            else
                u.getUlubioneMiejsca().add(o.get());
            users.save(u);
        }
    }

    @GetMapping("/api/user/znalezioneMiejsca")
    Iterable<Obiekt> getZnalezionymiejsca(@RequestParam String key) {
        User u = getUserByKey(key);
        if(u==null)return null;
        return u.getZnalezioneMiejsca();
    }
    @PostMapping("/api/user/znalezioneMiejsca")
    void addZnalezionymiejsca(@RequestParam String key,@RequestBody Map<String,String> map) {
        User u = getUserByKey(key);
        if(u==null)return;
        if(map.containsKey("id")) {
            Optional<Obiekt> o = miejsca.findById(Long.parseLong(map.get("id")));
            if(o.isEmpty())return;
            u.getZnalezioneMiejsca().add(o.get());
            users.save(u);
        }
    }

    @GetMapping("/api/user/znalezioneZagadki")
    Iterable<Riddle> getZnalezionyzagadki(@RequestParam String key) {
        User u = getUserByKey(key);
        if(u==null)return null;
        return u.getZnalezioneZagadki();
    }
    @PostMapping("/api/user/znalezioneZagadki")
    void addZnalezionyzagadki(@RequestParam String key,@RequestBody Map<String,String> map) {
        User u = getUserByKey(key);
        if(u==null)return;
        if(map.containsKey("id")) {
            Optional<Riddle> o = riddles.findById(Long.parseLong(map.get("id")));
            if(o.isEmpty())return;
            u.getZnalezioneZagadki().add(o.get());
            users.save(u);
        }
    }




    User getUserByKey(String key) {
        if(key==null)return null;
        if(key.isEmpty())return null;
        if(loggedUsers.findByToken(key)==null)return null;
        LoggedUser lu = loggedUsers.findByToken(key);
        if(lu==null)return null;
        return lu.getUser();
    }
    boolean isKeyAdminLogged(String key) {
        User u = getUserByKey(key);
        if(u==null)
            return false;
        return u.isAdmin();
    }

    private String RandomString(int i) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(i);
        for (int j = 0; j < i; j++) {
            sb.append(AB.charAt((int)(Math.random() * AB.length())));
        }
        return sb.toString();
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

}
