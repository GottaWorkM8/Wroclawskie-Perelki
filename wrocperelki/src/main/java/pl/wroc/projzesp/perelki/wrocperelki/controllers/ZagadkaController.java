package pl.wroc.projzesp.perelki.wrocperelki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wroc.projzesp.perelki.wrocperelki.exceptionAdvice.ZagadkaNotFoundException;
import pl.wroc.projzesp.perelki.wrocperelki.data.Zagadka;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.ZagadkaRepository;

import java.util.List;

@RestController
public class ZagadkaController {
    @Autowired
    private ZagadkaRepository repository;

    ZagadkaController(ZagadkaRepository repository) {
        this.repository = repository;
    }


    //pobieranie wszystkich zagadek
    @GetMapping("/api/zagadka")
    List<Zagadka> all() {
        return repository.findAll();
    }

    //wkładanie nowej zagadki
    @PostMapping("/api/zagadka")
    Zagadka newZagadka(@RequestBody Zagadka newEmployee) {
        return repository.save(newEmployee);
    }

    //pobieranie ile jest zagadek
    @GetMapping("/api/zagadka/getCount")
    int countZagadka() {
        return repository.findAll().size();
    }


    //pobieranie jednej zagadki
    @GetMapping("/api/zagadka/{id}")
    Zagadka one(@PathVariable Long id) throws ZagadkaNotFoundException {

        return repository.findById(id)
                .orElseThrow(() -> new ZagadkaNotFoundException(id));
    }


    //edycja zagadki
    @PutMapping("/api/zagadka/{id}")
    Zagadka replaceEmployee(@RequestBody Zagadka newEmployee, @PathVariable Long id) {

        return repository.findById(id)
                .map(employee -> {
                    employee.setCategory(employee.getCategory());
                    employee.setDescription(employee.getDescription());
                    employee.setCongratulations(employee.getCongratulations());
                    employee.setTitle(employee.getTitle());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
    }

    //Usówanie raczej nie, lepiej zmienić widoczność
    //@DeleteMapping("/api/zagadka/{id}")
    //void deleteEmployee(@PathVariable Long id) {
    //    repository.deleteById(id);
    //}
}
