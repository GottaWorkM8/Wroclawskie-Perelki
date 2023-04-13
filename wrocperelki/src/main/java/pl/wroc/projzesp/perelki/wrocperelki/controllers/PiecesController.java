package pl.wroc.projzesp.perelki.wrocperelki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wroc.projzesp.perelki.wrocperelki.data.Pieces;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.PiecesRepository;

import java.util.List;

@RestController
public class PiecesController {
    @Autowired
    private PiecesRepository miejsca;

    //pobieranie wszystkich miejsc
    @GetMapping("/api/miejsca")
    public List<Pieces> getMiejsca() {
        return miejsca.findAll();
    }

    //wkładanie nowego miejsca
    @PostMapping("/api/miejsca")
    Pieces newMiejsce(@RequestBody Pieces newPieces) {
        //todo identification
        //todo add user as author of this
        return miejsca.save(newPieces);
    }

    //pobieranie jednego miejsca
    @GetMapping("/api/miejsca/{id}")
    public Pieces getMiejsce(@PathVariable Long id) throws Exception {
        return miejsca.findById(id)
                .orElseThrow(() -> new Exception(String.valueOf(id)));
    }

    //pobieranie ile jest miejsc
    @GetMapping("/api/miejsca/getCount")
    int countMiejsce() {
        //todo not count invisible
        return miejsca.findAll().size();
    }

    //pobieranie ile jest miejsc należących do danej zagadki
    @GetMapping(value={"/api/miejsca/getCount/{id}", "/api/zagadka/{id}/getCount"})
    long countMiejsceZagadki(@PathVariable Long id) {
        //todo not count invisible
        return miejsca.getReferenceById(id).getRiddle_Pieces().size();
    }

    //edycja miejsca
    @PutMapping("/api/miejsca/{id}")
    Pieces replaceMiejsce(@RequestBody Pieces newPieces, @PathVariable Long id) {
/*
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.azimuth = azimuth;
        this.pitch = pitch;
        this.roll = roll;
        this.verify_photo = verify_photo;
        this.photo = photo;
        this.website = website;
        this.points = points;
 */
        //todo identification
        //todo check if user is autor of this
        return miejsca.findById(id)
                .map(miejsce -> {
                    miejsce.setName(newPieces.getName());
                    miejsce.setLatitude(newPieces.getLatitude());
                    miejsce.setLongitude(newPieces.getLongitude());
                    miejsce.setAzimuth(newPieces.getAzimuth());
                    miejsce.setPitch(newPieces.getPitch());
                    miejsce.setRoll(newPieces.getRoll());
                    miejsce.setVerify_photo(newPieces.getVerify_photo());
                    miejsce.setPhoto(newPieces.getPhoto());
                    miejsce.setWebsite(newPieces.getWebsite());
                    miejsce.setPoints(newPieces.getPoints());
                    return miejsca.save(miejsce);
                })
                .orElseGet(() -> {
                    newPieces.setPiece_id(id);
                    return miejsca.save(newPieces);
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
