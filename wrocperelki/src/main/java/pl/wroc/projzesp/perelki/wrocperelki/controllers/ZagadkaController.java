package pl.wroc.projzesp.perelki.wrocperelki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wroc.projzesp.perelki.wrocperelki.data.Pieces;
import pl.wroc.projzesp.perelki.wrocperelki.data.Riddle_Pieces;
import pl.wroc.projzesp.perelki.wrocperelki.data.Riddles;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.RiddlesRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ZagadkaController {
    @Autowired
    private RiddlesRepository repository;

    ZagadkaController(RiddlesRepository repository) {
        this.repository = repository;
    }


    //pobieranie wszystkich zagadek
    @GetMapping("/api/zagadka")
    List<Riddles> all() {
        return repository.findAll();
    }

    //wkładanie nowej zagadki
    @PostMapping("/api/zagadka")
    Riddles newZagadka(@RequestBody Riddles newEmployee) {
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
    Riddles one(@PathVariable Long id) throws Exception {

        return repository.findById(id)
                .orElseThrow(() -> new Exception(String.valueOf(id)));
    }
    @GetMapping(value={"/api/zagadka/{id}/getMiejsca"})
    List<Pieces> countMiejsceZagadki(@PathVariable Long id) {
        //todo not count invisible
        return repository.getReferenceById(id).getRiddle_Pieces().stream().map(Riddle_Pieces::getPiece_id).collect(Collectors.toList());
    }

    //edycja zagadki
    @PutMapping("/api/zagadka/{id}")
    Riddles replaceEmployee(@RequestBody Riddles newZagadka, @PathVariable Long id) {
/*
    private String name;
    private String category;
    private String info;
    private String congrats;
    private Long points;
 */
        //todo identification
        //todo check if user is autor of this
        return repository.findById(id)
                .map(zagadka -> {
                    zagadka.setName(newZagadka.getName());
                    zagadka.setCategory(newZagadka.getCategory());
                    zagadka.setInfo(newZagadka.getInfo());
                    zagadka.setCongrats(newZagadka.getCongrats());
                    zagadka.setPoints(newZagadka.getPoints());
                    return repository.save(newZagadka);
                })
                .orElseGet(() -> {
                    newZagadka.setRiddle_id(id);
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
}
