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
        pl.szajsjem.SimpleLog.log("Nowa zagadka od autora:"+u.getLogin());
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
                    if(!zagadka.getAuthor().equals(u.getLogin()))
                        throw new RuntimeException("Nie masz uprawnień");
                    zagadka.setId(id);
                    zagadka.setDifficulty(newZagadka.getDifficulty());
                    zagadka.setName(newZagadka.getName());
                    zagadka.setObjectCount(countMiejsceZagadki(id).size());
                    zagadka.setInfolink(newZagadka.getInfolink());
                    zagadka.setPoints(newZagadka.getPoints());
                    pl.szajsjem.SimpleLog.log("Edycja zagadki przez:"+u.getLogin()+" o id:"+id);
                    return riddleRepository.save(zagadka);
                })
                .orElseGet(() -> {
                    newZagadka.setId(id);
                    pl.szajsjem.SimpleLog.log("Nowa zagadka przez:"+u.getLogin()+" o id:"+id);
                    return riddleRepository.save(newZagadka);
                });
    }

    @PutMapping("/api/zagadka/{id}/addMiejsce/{idMiejsca}")
    Riddle connectMiejsce(@PathVariable Long id, @PathVariable Long idMiejsca,@RequestParam String key) throws Exception {
        User u = userController.getUser(key);
        if(u==null)return null;
        Riddle zagadka = riddleRepository.getReferenceById(id);
        Obiekt obiekt = miejsca.getReferenceById(idMiejsca);

        if(zagadka.getAuthor()!=null) {
            if(!zagadka.getAuthor().equals(u.getLogin()))
                return null;
        }
        else{
            zagadka.setAuthor(u.getLogin());
        }
        if(obiekt.getAuthor()!=null) {
            if(!obiekt.getAuthor().equals(u.getLogin()))
                return null;
        }
        else{
            obiekt.setAuthor(u.getLogin());
        }
        obiekt.setId(idMiejsca);
        zagadka.setId(id);
        obiekt.setRiddles(zagadka);
        miejsca.save(obiekt);
        zagadka.setObjectCount(countMiejsceZagadki(id).size());
        pl.szajsjem.SimpleLog.log("połączenie zagadki"+" o id:"+zagadka.getId()+" i miejsca"+" o id:"+obiekt.getId()+" przez:"+u.getLogin());
        return riddleRepository.save(zagadka);
    }

    //Usówanie raczej nie, lepiej zmienić widoczność
    @DeleteMapping("/api/zagadka/{id}")
    void deleteEmployee(@PathVariable Long id,@RequestParam String key) throws Exception {
        User u = userController.getUser(key);
        if(u==null)return;
        if(!u.isAdmin()){
            Riddle r = riddleRepository.findById(id).get();
            if (!r.getAuthor().equals(u.getLogin())) return;
        }
        pl.szajsjem.SimpleLog.log("Usunięcie zagadki"+" o id:"+id +" przez:"+u.getLogin());
        riddleRepository.deleteById(id);
    }

    @GetMapping(value={"/api/zagadka/{id}/nieZnalezioneObiekty"})
    Set<Obiekt> miejscenieZagadki(@PathVariable Long id,@RequestParam String key) throws Exception {
        User u = userController.getUser(key);
        if(u==null)return null;
        Set<Obiekt> nalezaceDoZagadki= countMiejsceZagadki(id);
        Set<Obiekt> wszystkieZnalezione = u.getZnalezioneMiejsca();
        return nalezaceDoZagadki.stream().filter(obiekt -> !wszystkieZnalezione.contains(obiekt)).collect(Collectors.toSet());
    }
    @GetMapping(value={"/api/zagadka/{id}/znalezioneObiekty"})
    Set<Obiekt> miejsceZagadki(@PathVariable Long id,@RequestParam String key) throws Exception {
        User u = userController.getUser(key);
        if(u==null)return null;
        Set<Obiekt> nalezaceDoZagadki= countMiejsceZagadki(id);
        Set<Obiekt> wszystkieZnalezione = u.getZnalezioneMiejsca();
        return nalezaceDoZagadki.stream().filter(wszystkieZnalezione::contains).collect(Collectors.toSet());
    }




    //@GetMapping("/api/zagadka/{id}/getRoadLength")
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

    @PostMapping("/api/zagadka/{id}/refreshCount")
    public void refreshZagadka(Long id) {
        Riddle zagadka = riddleRepository.getReferenceById(id);
        zagadka.setObjectCount(countMiejsceZagadki(id).size());
        riddleRepository.save(zagadka);
        pl.szajsjem.SimpleLog.log("Odświerzenie object count dla id:"+id.toString());
    }
}
