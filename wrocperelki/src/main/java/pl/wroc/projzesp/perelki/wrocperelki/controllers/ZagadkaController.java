package pl.wroc.projzesp.perelki.wrocperelki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wroc.projzesp.perelki.wrocperelki.data.Riddles;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.RiddlesRepository;

import java.util.List;

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
        return repository.save(newEmployee);
    }

    //pobieranie ile jest zagadek
    @GetMapping("/api/zagadka/getCount")
    int countZagadka() {
        return repository.findAll().size();
    }


    //pobieranie jednej zagadki
    @GetMapping("/api/zagadka/{id}")
    Riddles one(@PathVariable Long id) throws Exception {

        return repository.findById(id)
                .orElseThrow(() -> new Exception(String.valueOf(id)));
    }


    //edycja zagadki
    @PutMapping("/api/zagadka/{id}")
    Riddles replaceEmployee(@RequestBody Riddles newEmployee, @PathVariable Long id) {
/*
    private String name;
    private String category;
    private String info;
    private String congrats;
    private Long points;
 */
        return repository.findById(id)
                .map(zagadka -> {
                    zagadka.setName(zagadka.getName());
                    zagadka.setCategory(zagadka.getCategory());
                    zagadka.setInfo(zagadka.getInfo());
                    zagadka.setCongrats(zagadka.getCongrats());
                    zagadka.setPoints(zagadka.getPoints());
                    return repository.save(zagadka);
                })
                .orElseGet(() -> {
                    newEmployee.setRiddle_id(id);
                    return repository.save(newEmployee);
                });
    }

    //Usówanie raczej nie, lepiej zmienić widoczność
    //@DeleteMapping("/api/zagadka/{id}")
    //void deleteEmployee(@PathVariable Long id) {
    //    repository.deleteById(id);
    //}
}
