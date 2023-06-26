package pl.wroc.projzesp.perelki.wrocperelki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wroc.projzesp.perelki.wrocperelki.model.Obiekt;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.ObiektRepository;
import pl.wroc.projzesp.perelki.wrocperelki.model.User;

import java.util.List;

@RestController
public class ObiektyController {
    @Autowired
    private ObiektRepository miejsca;
    @Autowired
    private UserController  userController;
    @Autowired
    private ZagadkaController  zagadkaController;

    @GetMapping("/api/miejsca")
    public List<Obiekt> getMiejsca() {
        return miejsca.findAll();
    }

    @PostMapping("/api/miejsca")
    Obiekt newMiejsce(@RequestBody Obiekt newObiekt,@RequestParam String key) throws Exception {
        User u = userController.getUser(key);
        if(u==null)return null;
        newObiekt.setAuthor(u.getLogin());
        if(newObiekt.getRiddles()!=null){
            if(!zagadkaController.oneZagadka(newObiekt.getRiddles().getId()).getAuthor().equals(u.getLogin())) {
                pl.szajsjem.SimpleLog.log("próba: Nowy obiekt przez:"+u.getLogin()+" err: not your zagadka");
                return null;
            }
        }
        pl.szajsjem.SimpleLog.log("Nowy obiekt przez:"+u.getLogin());
        Obiekt o = miejsca.save(newObiekt);
        if(newObiekt.getRiddles()!=null) {
            zagadkaController.refreshZagadka(newObiekt.getRiddles().getId());
        }
        return o;
    }

    @GetMapping("/api/miejsca/{id}")
    public Obiekt getMiejsce(@PathVariable Long id) throws Exception {
        return miejsca.findById(id)
                .orElseThrow(() -> new Exception(String.valueOf(id)));
    }

    @GetMapping("/api/miejsca/getCount")
    int countMiejsce() {
        //todo not count invisible
        return miejsca.findAll().size();
    }

    @PutMapping("/api/miejsca/{id}")
    Obiekt replaceMiejsce(@RequestBody Obiekt newObiekt, @PathVariable Long id,@RequestParam String key) throws Exception {
        User u = userController.getUser(key);
        if(u==null)return null;
        newObiekt.setAuthor(u.getLogin());
        return miejsca.findById(id)
                .map(miejsce -> {
                    if(miejsce.getAuthor()!=null) {
                        if (!miejsce.getAuthor().equals(u.getLogin()))
                            throw new RuntimeException("not yours");
                    }
                    else{
                        miejsce.setAuthor(u.getLogin());
                    }
                    miejsce.setId(id);
                    miejsce.setObjectName(newObiekt.getObjectName());
                    miejsce.setObjectPosition(newObiekt.getObjectPosition());
                    miejsce.setTrackingPosition(newObiekt.getTrackingPosition());
                    miejsce.setPhotoPosition(newObiekt.getPhotoPosition());
                    miejsce.setPhotoShowRadius(newObiekt.getPhotoShowRadius());
                    miejsce.setTelephoneOrientation(newObiekt.getTelephoneOrientation());
                    miejsce.setPhotoObjectLink(newObiekt.getPhotoObjectLink());
                    miejsce.setPhotoLink(newObiekt.getPhotoLink());
                    miejsce.setInfoLink(newObiekt.getInfoLink());
                    miejsce.setVisible(newObiekt.isVisible());
                    pl.szajsjem.SimpleLog.log("Update obiektu o id:"+id+" przez:"+u.getLogin());
                    Obiekt r = miejsca.save(miejsce);
                    if(miejsce.getRiddles()!=null) {
                        zagadkaController.refreshZagadka(miejsce.getRiddles().getId());
                    }
                    return r;
                })
                .orElseGet(() -> {
                    newObiekt.setId(id);
                    pl.szajsjem.SimpleLog.log("Nowy obiekt o id:"+id+" przez:"+u.getLogin());
                    Obiekt r = miejsca.save(newObiekt);
                    if(newObiekt.getRiddles()!=null) {
                        zagadkaController.refreshZagadka(newObiekt.getRiddles().getId());
                    }
                    return r;
                });
    }


    @DeleteMapping("/api/miejsca/{id}")
    void deleteEmployee(@PathVariable Long id,@RequestParam String key) throws Exception {
        User u = userController.getUser(key);
        if(u==null)return;
        if(!u.isAdmin())
            if(miejsca.findById(id).get().getAuthor().equals(u.getLogin()))
                return;
        pl.szajsjem.SimpleLog.log("Usówanie obiektu o id:"+id.toString()+" przez:"+u.getLogin());
        miejsca.deleteById(id);
    }

}
