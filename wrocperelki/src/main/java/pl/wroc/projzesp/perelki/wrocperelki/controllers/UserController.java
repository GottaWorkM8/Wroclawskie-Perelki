package pl.wroc.projzesp.perelki.wrocperelki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.PiecesRepository;
import pl.wroc.projzesp.perelki.wrocperelki.interfaces.UsersRepository;

@RestController
public class UserController {

    @Autowired
    private UsersRepository users;





}
