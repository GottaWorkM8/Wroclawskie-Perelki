package pl.wroc.projzesp.perelki.wrocperelki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wroc.projzesp.perelki.wrocperelki.model.Obiekt;
import pl.wroc.projzesp.perelki.wrocperelki.model.Riddle;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.RiddleRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ZagadkaController {
    @Autowired
    private RiddleRepository repository;
    /*
    private Long id;
    private String difficulty;
    private String name;
    private int objectCount;
    private String infolink;
    private String author;
    private int points;
     */

    ZagadkaController(RiddleRepository repository) {
        this.repository = repository;
    }


    //pobieranie wszystkich zagadek
    @GetMapping("/api/zagadka")
    List<Riddle> all() {
        return repository.findAll();
    }

    //wkładanie nowej zagadki
    @PostMapping("/api/zagadka")
    Riddle newZagadka(@RequestBody Riddle newEmployee) {
        //todo identification
        //todo add user as author of this
        return repository.save(newEmployee);
    }

    //pobieranie ile jest zagadek
    @GetMapping("/api/zagadka/getCount")
    long countZagadka() {
        return repository.count();
    }


    //pobieranie jednej zagadki
    @GetMapping("/api/zagadka/{id}")
    Riddle one(@PathVariable Long id) throws Exception {

        return repository.findById(id)
                .orElseThrow(() -> new Exception(String.valueOf(id)));
    }
    @GetMapping(value={"/api/zagadka/{id}/getMiejsca"})
    List<Obiekt> countMiejsceZagadki(@PathVariable Long id) {
        return repository.getReferenceById(id).getObiekty().stream().filter(Obiekt::isVisible).collect(Collectors.toList());
    }

    //edycja zagadki
    @PutMapping("/api/zagadka/{id}")
    Riddle replaceEmployee(@RequestBody Riddle newZagadka, @PathVariable Long id) {
        //todo identification
        //todo check if user is autor of this, or admin
        return repository.findById(id)
                .map(zagadka -> {
                    zagadka.setId(id);
                    zagadka.setDifficulty(newZagadka.getDifficulty());
                    zagadka.setName(newZagadka.getName());
                    zagadka.setObjectCount(countMiejsceZagadki(id).size());
                    zagadka.setInfolink(newZagadka.getInfolink());
                    zagadka.setAuthor(newZagadka.getAuthor());
                    zagadka.setPoints(newZagadka.getPoints());
                    return repository.save(zagadka);
                })
                .orElseGet(() -> {
                    newZagadka.setId(id);
                    return repository.save(newZagadka);
                });
    }

    //Usówanie raczej nie, lepiej zmienić widoczność
    @DeleteMapping("/api/zagadka/{id}")
    void deleteEmployee(@PathVariable Long id) {
        //todo identification
        //todo check if user is admin
        repository.deleteById(id);
    }
    @GetMapping(value={"/api/zagadka/{id}/getPoints"})
    int getPoints(@PathVariable Long id) {
        return repository.getReferenceById(id).getPoints();
    }
    @GetMapping(value={"/api/zagadka/{id}/getAuthor"})
    String getAuthor(@PathVariable Long id) {
        return repository.getReferenceById(id).getAuthor();
    }
    @GetMapping(value={"/api/zagadka/{id}/getDifficulty"})
    String getDifficulty(@PathVariable Long id) {
        return repository.getReferenceById(id).getDifficulty();
    }
    @GetMapping(value={"/api/zagadka/{id}/getName"})
    String getName(@PathVariable Long id) {
        return repository.getReferenceById(id).getName();
    }
    @GetMapping(value={"/api/zagadka/{id}/getObjectCount"})
    int getObjectCount(@PathVariable Long id) {
        return repository.getReferenceById(id).getObjectCount();
    }
    @GetMapping(value={"/api/zagadka/{id}/getInfolink"})
    String getInfoLink(@PathVariable Long id) {
        return repository.getReferenceById(id).getInfolink();
    }
    @GetMapping(value={"/api/zagadka/{id}/getObiekty"})
    Set<Obiekt> getObiekty(@PathVariable Long id) {
        return repository.getReferenceById(id).getObiekty();
    }
}
