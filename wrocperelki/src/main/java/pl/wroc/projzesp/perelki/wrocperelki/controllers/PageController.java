package pl.wroc.projzesp.perelki.wrocperelki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class PageController {//brain 100

    @Autowired
    ZagadkaController zagadkaController;
    @Autowired
    MiejsceController miejsceController;

    private String readOrError(String file){
        try {
            return Files.readString(Path.of("src\\main\\resources\\"+file));
        }
        catch (Exception e){
            return "internal error";
        }
    }
    @GetMapping("/")
    public String getPage() {
        return readOrError("static\\index.html");
    }
    @GetMapping("/zagadki")
    public String getZagadki() {
        String[] tmp = readOrError("templates\\zagadki.html").split("##LISTA##");
        if(tmp.length==2) {
            StringBuilder lista= new StringBuilder();
            for(var t:zagadkaController.all()){
                lista.append("            <li>")
                        .append(t.getTitle())
                        .append(" | ")
                        .append(t.getDescription())
                        .append(" | <a href=\"/edytujZagadke?id=")
                        .append(t.getId())
                        .append("\">Edytuj</a> | <a href=\"/places?q=")
                        .append(t.getId())
                        .append("\">Miejsca zagadki</a></li>\n");
            }
            return tmp[0] + lista.toString() + tmp[1];
        }
        return "internal error";
    }
}
