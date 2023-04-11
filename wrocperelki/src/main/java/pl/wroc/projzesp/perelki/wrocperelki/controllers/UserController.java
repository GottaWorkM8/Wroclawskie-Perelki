package pl.wroc.projzesp.perelki.wrocperelki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wroc.projzesp.perelki.wrocperelki.data.Users;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.PiecesRepository;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.UsersRepository;

import java.util.Map;
import java.util.Objects;

@RestController
public class UserController {

    @Autowired
    private UsersRepository users;

    @PostMapping("/api/user/testlogin")
    boolean login(@RequestBody Map<String,String> map) {
        if(map.isEmpty())return false;
        long id=0;
        for(var t:users.findAll()){
            if(Objects.equals(t.getEmail(), map.get("email"))){
                id=t.getUser_id();
                break;
            }
            if(Objects.equals(t.getLogin(), map.get("login"))){
                id=t.getUser_id();
                break;
            }
        }
        if(id>0){
            try {
                Users u = users.findById(id).get();
                if (Objects.equals(u.getPassword(), map.get("password"))) {
                    return true;
                }
            }
            catch(Exception ignored){

            }
        }
        return false;
    }

    @PostMapping("/api/user/register")
    boolean register(@RequestBody Map<String,String> map) {
        if(map.isEmpty())return false;
        if(!map.containsKey("login"))return false;
        if(!map.containsKey("password"))return false;
        if(!map.containsKey("email"))return false;
        if(!map.containsKey("phone"))return false;
        //todo check if user already exist
        users.save(new Users(0L,map.get("login"),map.get("password"),map.get("email"),map.get("phone"),"user",0L));
        return false;
    }

    @GetMapping("/api/user/{id}")
    Users getUser(@PathVariable Long id) throws Exception {
        //todo identification
        return users.findById(id)
                .orElseThrow(() -> new Exception(String.valueOf(id)));
    }


}
