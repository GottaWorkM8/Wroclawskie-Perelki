package pl.wroc.projzesp.perelki.wrocperelki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wroc.projzesp.perelki.wrocperelki.model.Obiekt;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.ObiektRepository;

import java.util.List;

@RestController
public class ObiektyController {
    @Autowired
    private ObiektRepository miejsca;
    /*
    private Long id ;
    private String objectName ;
    private String objectPosition ;
    private String trackingPosition ;
    private String photoPosition ;
    private String photoShowRadius ;
    private String telephoneOrientation ;
    private String photoObjectLink  ;
    private String photoLink  ;
    private String infoLink  ;
    private boolean visible ;
     */

    //pobieranie wszystkich miejsc
    @GetMapping("/api/miejsca")
    public List<Obiekt> getMiejsca() {
        return miejsca.findAll();
    }

    //wkładanie nowego miejsca
    @PostMapping("/api/miejsca")
    Obiekt newMiejsce(@RequestBody Obiekt newObiekt) {
        //todo identification
        //todo add user as author of this
        return miejsca.save(newObiekt);
    }

    //pobieranie jednego miejsca
    @GetMapping("/api/miejsca/{id}")
    public Obiekt getMiejsce(@PathVariable Long id) throws Exception {
        return miejsca.findById(id)
                .orElseThrow(() -> new Exception(String.valueOf(id)));
    }

    //pobieranie ile jest miejsc
    @GetMapping("/api/miejsca/getCount")
    int countMiejsce() {
        //todo not count invisible
        return miejsca.findAll().size();
    }

    //edycja miejsca
    @PutMapping("/api/miejsca/{id}")
    Obiekt replaceMiejsce(@RequestBody Obiekt newObiekt, @PathVariable Long id) {
        //todo identification
        //todo check if user is autor of this
        return miejsca.findById(id)
                .map(miejsce -> {
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
                    return miejsca.save(miejsce);
                })
                .orElseGet(() -> {
                    newObiekt.setId(id);
                    return miejsca.save(newObiekt);
                });
    }


    //Usówanie raczej nie, lepiej zmienić widoczność
    @DeleteMapping("/api/miejsca/{id}")
    void deleteEmployee(@PathVariable Long id) {
        //todo identification
        //todo check if user is admin
        miejsca.deleteById(id);
    }

}
