package pl.wroc.projzesp.perelki.wrocperelki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wroc.projzesp.perelki.wrocperelki.data.Miejsce;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.MiejsceRepository;

import java.util.List;

@RestController
public class MiejsceController {
    @Autowired
    private MiejsceRepository miejsca;
    @GetMapping("/api/miejsce/getAll")
    public List<Miejsce> getMiejsca() {
        return miejsca.findAll();
    }
}
