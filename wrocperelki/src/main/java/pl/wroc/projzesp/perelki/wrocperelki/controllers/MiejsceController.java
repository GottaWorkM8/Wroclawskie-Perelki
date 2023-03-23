package pl.wroc.projzesp.perelki.wrocperelki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wroc.projzesp.perelki.wrocperelki.data.Miejsce;
import pl.wroc.projzesp.perelki.wrocperelki.exceptionAdvice.MiejsceNotFoundException;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.MiejsceRepository;

import java.util.List;

@RestController
public class MiejsceController {
    @Autowired
    private MiejsceRepository miejsca;

    //pobieranie wszystkich miejsc
    @GetMapping("/api/miejsca")
    public List<Miejsce> getMiejsca() {
        return miejsca.findAll();
    }

    //wkładanie nowego miejsca
    @PostMapping("/api/miejsca")
    Miejsce newMiejsce(@RequestBody Miejsce newMiejsce) {
        return miejsca.save(newMiejsce);
    }

    //pobieranie jednego miejsca
    @GetMapping("/api/miejsca/{id}")
    public Miejsce getMiejsce(@PathVariable Long id) {
        return miejsca.findById(id)
                .orElseThrow(() -> new MiejsceNotFoundException(id));
    }

    //pobieranie ile jest miejsc
    @GetMapping("/api/miejsca/getCount")
    int countMiejsce() {
        return miejsca.findAll().size();
    }

    //pobieranie ile jest miejsc należących do danej zagadki
    @GetMapping(value={"/api/miejsca/getCount/{id}", "/api/zagadka/{id}/getCount"})
    long countMiejsceZagadki(@PathVariable Long id) {
        return miejsca.findAll().stream().filter(o -> o.getZagadka().getId()==id).count();
    }

    //edycja miejsca
    @PutMapping("/api/miejsca/{id}")
    Miejsce replaceMiejsce(@RequestBody Miejsce newMiejsce, @PathVariable Long id) {

        return miejsca.findById(id)
                .map(miejsce -> {
                    miejsce.setName(newMiejsce.getName());
                    miejsce.setTrackingPosition(newMiejsce.getTrackingPosition());
                    miejsce.setRadius(newMiejsce.getRadius());
                    miejsce.setPhotoLink(newMiejsce.getPhotoLink());
                    miejsce.setPhotoGps(newMiejsce.getPhotoGps());
                    miejsce.setTelephonePosition(newMiejsce.getTelephonePosition());
                    miejsce.setCorrectFoundLink(newMiejsce.getCorrectFoundLink());
                    return miejsca.save(miejsce);
                })
                .orElseGet(() -> {
                    newMiejsce.setId(id);
                    return miejsca.save(newMiejsce);
                });
    }


}
