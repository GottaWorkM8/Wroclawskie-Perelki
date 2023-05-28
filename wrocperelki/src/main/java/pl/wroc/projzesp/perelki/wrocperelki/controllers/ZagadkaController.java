package pl.wroc.projzesp.perelki.wrocperelki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.ObiektRepository;
import pl.wroc.projzesp.perelki.wrocperelki.model.Obiekt;
import pl.wroc.projzesp.perelki.wrocperelki.model.Riddle;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.RiddleRepository;
import pl.wroc.projzesp.perelki.wrocperelki.model.User;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ZagadkaController {
    @Autowired
    private RiddleRepository riddleRepository;
    /*
    private Long id;
    private String difficulty;
    private String name;
    private int objectCount;
    private String infolink;
    private String author;
    private int points;
     */
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
    
    @Autowired
    private UserController  userController;


    //pobieranie wszystkich zagadek
    @GetMapping("/api/zagadka")
    List<Riddle> all() {
        return riddleRepository.findAll();
    }

    //wkładanie nowej zagadki
    @PostMapping("/api/zagadka")
    Riddle newZagadka(@RequestBody Riddle newEmployee,@RequestParam String key) throws Exception {
        User u = userController.getUser(key);
        if(u==null)return null;
        newEmployee.setAuthor(u.getLogin());
        return riddleRepository.save(newEmployee);
    }

    //pobieranie ile jest zagadek
    @GetMapping("/api/zagadka/getCount")
    long countZagadka() {
        return riddleRepository.count();
    }


    //pobieranie jednej zagadki
    @GetMapping("/api/zagadka/{id}")
    Riddle oneZagadka(@PathVariable Long id) throws Exception {

        return riddleRepository.findById(id)
                .orElseThrow(() -> new Exception(String.valueOf(id)));
    }
    @GetMapping(value={"/api/zagadka/{id}/getMiejsca"})
    Set<Obiekt> countMiejsceZagadki(@PathVariable Long id) {
        return miejsca.findAll().stream().filter(Obiekt::isVisible).filter(obiekt -> obiekt.getRiddles()!=null).filter(obiekt -> Objects.equals(obiekt.getRiddles().getId(), id)).collect(Collectors.toSet());//riddleRepository.getReferenceById(id).getObiekty();//.stream().filter(Obiekt::isVisible).collect(Collectors.toList());
    }

    //edycja zagadki
    @PutMapping("/api/zagadka/{id}")
    Riddle replaceEmployee(@RequestBody Riddle newZagadka, @PathVariable Long id,@RequestParam String key) throws Exception {
        User u = userController.getUser(key);
        if(u==null)return null;
        newZagadka.setAuthor(u.getLogin());
        return riddleRepository.findById(id)
                .map(zagadka -> {
                    if(zagadka.getAuthor().equals(u.getLogin()))
                        throw new RuntimeException("Nie masz uprawnień");
                    zagadka.setId(id);
                    zagadka.setDifficulty(newZagadka.getDifficulty());
                    zagadka.setName(newZagadka.getName());
                    zagadka.setObjectCount(countMiejsceZagadki(id).size());
                    zagadka.setInfolink(newZagadka.getInfolink());
                    zagadka.setPoints(newZagadka.getPoints());
                    return riddleRepository.save(zagadka);
                })
                .orElseGet(() -> {
                    newZagadka.setId(id);
                    return riddleRepository.save(newZagadka);
                });
    }

    @PutMapping("/api/zagadka/{id}/addMiejsce/{idMiejsca}")
    Riddle connectMiejsce(@PathVariable Long id, @PathVariable Long idMiejsca,@RequestParam String key) throws Exception {
        User u = userController.getUser(key);
        if(u==null)return null;
        Riddle zagadka = riddleRepository.getReferenceById(id);
        Obiekt obiekt = miejsca.getReferenceById(idMiejsca);
        if(!zagadka.getAuthor().equals(u.getLogin()))
            return null;
        if(!obiekt.getAuthor().equals(u.getLogin()))
            return null;
        obiekt.setId(idMiejsca);
        zagadka.setId(id);
        obiekt.setRiddles(zagadka);
        miejsca.save(obiekt);
        zagadka.setObjectCount(countMiejsceZagadki(id).size());
        return riddleRepository.save(zagadka);
    }

    //Usówanie raczej nie, lepiej zmienić widoczność
    @DeleteMapping("/api/zagadka/{id}")
    void deleteEmployee(@PathVariable Long id,@RequestParam String key) throws Exception {
        User u = userController.getUser(key);
        if(u==null)return;
        Riddle r = riddleRepository.findById(id).get();
        if(!r.getAuthor().equals(u.getLogin()))return;
        riddleRepository.deleteById(id);
    }






    @GetMapping("/api/zagadka/{id}/getRoadLength")
    float odlegloscZagadki(@PathVariable Long id,@RequestParam String gpsPosition) {
        Riddle zagadka = riddleRepository.getReferenceById(id);
        Set<Obiekt> miejscaZagadki = zagadka.getObiekty();
        //Set<Obiekt> miejscaZagadkiWyjscie = miejscaZagadki.stream().filter(obiekt -> obiekt.isVisible()).collect(Collectors.toSet());
        float minodleglosc = 10000000.0f;
        for (Obiekt obiekt : miejscaZagadki) {
            float odleglosc = obiekt.odlegloscDoPunktu(gpsPosition);
            if (odleglosc < minodleglosc) {
                minodleglosc = odleglosc;
            }
        }
        return minodleglosc;
    }
}
