package pl.wroc.projzesp.perelki.wrocperelki.controllers;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import pl.wroc.projzesp.perelki.wrocperelki.data.Miejsce;
import pl.wroc.projzesp.perelki.wrocperelki.data.Zagadka;
import pl.wroc.projzesp.perelki.wrocperelki.exceptionAdvice.ZagadkaNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

@RestController
public class PageController {

    @Autowired//brain 10
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



    @GetMapping("/style.css")
    public String getstyle() {
        return readOrError("static\\style.css");
    }
    @GetMapping(
            value = "/Media/{name}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getMedia(@PathVariable String name) throws Exception {
        InputStream in = getClass()
                .getResourceAsStream("/static/Media/"+name);
        if(in==null)throw new Exception("internal error");
        return IOUtils.toByteArray(in);
    }

    @GetMapping("/download")
    public String getDownload() {
        return readOrError("static\\download.html");
    }
    @GetMapping("/riddles")
    public String getriddles() {
        String[] tmp = readOrError("templates\\zagadki\\riddles.html").split("##LISTA##");
        if(tmp.length==2) {
            StringBuilder lista= new StringBuilder();
            for(var t:zagadkaController.all()){
                lista.append("            <li><a href=\"/riddles/")
                        .append(t.getId())
                        .append("\">")
                        .append(t.getTitle())
                        .append("</a> | ")
                        .append(t.getDescription())
                        .append(" | <a href=\"/riddles/e/")
                        .append(t.getId())
                        .append("\">Edytuj</a> | <a href=\"/places/q/")
                        .append(t.getId())
                        .append("\">Miejsca zagadki</a></li>\n");
            }
            return tmp[0] + lista.toString() + tmp[1];
        }
        return "internal error";
    }
    @PostMapping("/riddles/{id}")
    public String putriddle(@PathVariable Long id,@RequestParam Map<String, String> paramMap) {
        if(paramMap==null)return "no data";
        Zagadka z =zagadkaController.one(id);
        if(paramMap.containsKey("title"))
            z.setTitle(paramMap.get("title"));
        if(paramMap.containsKey("category"))
            z.setCategory(paramMap.get("category"));
        if(paramMap.containsKey("description"))
            z.setDescription(paramMap.get("description"));
        if(paramMap.containsKey("congratulations"))
            z.setCongratulations(paramMap.get("congratulations"));
        zagadkaController.replaceEmployee(z,id);
        return getriddle(id);
    }
    @GetMapping("/riddles/{id}")
    public String getriddle(@PathVariable Long id) {//////////////////////////////////////////////////////////////////////////////////////////////////////
        Zagadka z =zagadkaController.one(id);
        return z.getTitle()+"<br>"+z.getCategory()+"<br>"+z.getDescription()+"<br>"+z.getCongratulations();
    }

    @GetMapping("/places/{id}")
    public String getplace(@PathVariable Long id) {
        var z =miejsceController.getMiejsce(id);
        return z.getName()+"<br>"+z.getTrackingPosition()+"<br>"+z.getRadius()+"<br>"+z.getTelephonePosition()+"<br>"+z.getCorrectFoundLink()+"<br>"+z.getPhotoGps()+"<br>"+z.getPhotoLink();
    }
    @GetMapping("/places")
    public String getPlaces() {
        String[] tmp = readOrError("templates\\places.html").split("##LISTA##");
        if(tmp.length==2) {
            StringBuilder lista= new StringBuilder();
            for(var t:miejsceController.getMiejsca()){
                lista.append("            <li><a href=\"/places/")
                        .append(t.getId())
                        .append("\">")
                        .append(t.getName())
                        .append("</a> | ")
                        .append(t.getTrackingPosition())
                        .append(" | <a href=\"")
                        .append(t.getPhotoLink())
                        .append("\">Zobacz zdjęcie</a> | <a href=\"/editPlaces?id=")
                        .append(t.getId())
                        .append("\">Edytuj</a>");
            }
            return tmp[0] + lista.toString() + tmp[1];
        }
        return "internal error";
    }
    @GetMapping("/places/q/{id}")
    public String getPlacesFiltered(@PathVariable Long id) {
        String[] tmp = readOrError("templates\\places.html").split("##LISTA##");
        if(tmp.length==2) {
            StringBuilder lista= new StringBuilder();
            for(var t:miejsceController.getMiejsca()){
                if(Objects.equals(t.getZagadka().getId(), id))
                    lista.append("            <li><a href=\"/places/")
                            .append(t.getId())
                            .append("\">")
                            .append(t.getName())
                            .append("</a> | ")
                            .append(t.getTrackingPosition())
                            .append(" | <a href=\"")
                            .append(t.getPhotoLink())
                            .append("\">Zobacz zdjęcie</a> | <a href=\"/editPlaces?id=")
                            .append(t.getId())
                            .append("\">Edytuj</a>");
            }
            return tmp[0] + lista.toString() + tmp[1];
        }
        return "internal error";
    }
    @GetMapping("/register")
    public String getregister() {
        return readOrError("static\\register.html");
    }
    @GetMapping("/signin")
    public String getsignin() {
        return readOrError("static\\sign-in.html");
    }


//    @GetMapping("/newriddle")
//    public String getnewRiddle() {
//        return readOrError("static\\new-riddle.html");
//    }




    @GetMapping(value = {"/regulamin","/error"})
    public String regulamin() {
        return readOrError("static\\regulamin.html");
    }
    @GetMapping(value={"/", "/home"})
    public String getPage() {
        return readOrError("static\\home.html");
    }




    @PostMapping("/riddles")
    String newZagadka(@RequestParam Map<String, String> paramMap) throws Exception {
        if(paramMap == null)throw new Exception("internal error");
        if(!paramMap.containsKey("title")
                ||!paramMap.containsKey("category")
                ||!paramMap.containsKey("description")
                ||!paramMap.containsKey("congratulations"))throw new Exception("internal error");
        Zagadka zagadka = new Zagadka();
        zagadka.setTitle((String) paramMap.get("title"));
        zagadka.setCategory((String) paramMap.get("category"));
        zagadka.setDescription((String) paramMap.get("description"));
        zagadka.setCongratulations((String) paramMap.get("congratulations"));
        zagadkaController.newZagadka(zagadka);
        return getriddles();
    }
//    @GetMapping("/zagadki")
//    public String getZagadki() {
//        String[] tmp = readOrError("templates\\zagadki.html").split("##LISTA##");
//        if(tmp.length==2) {
//            StringBuilder lista= new StringBuilder();
//            for(var t:zagadkaController.all()){
//                lista.append("            <li>")
//                        .append(t.getTitle())
//                        .append(" | ")
//                        .append(t.getDescription())
//                        .append(" | <a href=\"/edytujZagadke?id=")
//                        .append(t.getId())
//                        .append("\">Edytuj</a> | <a href=\"/places?q=")
//                        .append(t.getId())
//                        .append("\">Miejsca zagadki</a></li>\n");
//            }
//            return tmp[0] + lista.toString() + tmp[1];
//        }
//        return "internal error";
//    }
    @GetMapping("/newriddle")
    public String nowaZagadka() {
        return readOrError("static\\newRiddle.html");
    }
    @GetMapping("/riddles/e/{id}")
    public String edycjaZagadki(@PathVariable Long id) {
        Zagadka z =zagadkaController.one(id);
        return readOrError("templates\\editRiddle.html")
                .replaceAll("#id#",z.getId().toString())
                .replaceAll("#title#",z.getTitle())
                .replaceAll("#category#",z.getCategory())
                .replaceAll("#description#",z.getDescription())
                .replaceAll("#congratulations#",z.getCongratulations());
    }



    @PostMapping("/places")
    String newmiejsca(@RequestParam Map<String, String> paramMap) throws Exception {
        if(paramMap == null)throw new Exception("internal error");
        if(!paramMap.containsKey("trackingPosition")
                ||!paramMap.containsKey("radius")
                ||!paramMap.containsKey("photoLink")
                ||!paramMap.containsKey("photoGps")
                ||!paramMap.containsKey("telephonePosition")
                ||!paramMap.containsKey("correctFoundLink"))throw new Exception("internal error");
        Miejsce miejsce = new Miejsce();
        miejsce.setTrackingPosition((String) paramMap.get("trackingPosition"));
        miejsce.setRadius((String) paramMap.get("radius"));
        miejsce.setPhotoLink((String) paramMap.get("photoLink"));
        miejsce.setPhotoGps((String) paramMap.get("photoGps"));
        miejsce.setTelephonePosition((String) paramMap.get("telephonePosition"));
        miejsce.setCorrectFoundLink((String) paramMap.get("correctFoundLink"));
        miejsceController.newMiejsce(miejsce);
        return getPlaces();
    }
//    @GetMapping("/places")
//    public String getmiejsca() {
//        String[] tmp = readOrError("templates\\xplaces.html").split("##LISTA##");
//        if(tmp.length==2) {
//            StringBuilder lista= new StringBuilder();
//            for(var t:miejsceController.getMiejsca()){
//                lista.append("            <li>")
//                        .append(t.getName())
//                        .append(" | ")
//                        .append(t.getTrackingPosition())
//                        .append(" | <a href=\"")
//                        .append(t.getPhotoLink())
//                        .append("\">Zobacz zdjęcie</a> | <a href=\"/editPlaces?id=")
//                        .append(t.getId())
//                        .append("\">Edytuj</a>");
//            }
//            return tmp[0] + lista.toString() + tmp[1];
//        }
//        return "internal error";
//    }
    @GetMapping("/addPlaces")
    public String nowemiejsce() {
        return readOrError("static\\noweM.html");
    }
    @GetMapping("/editPlaces")
    public String edycjamiejsca() {
        return readOrError("templates\\edycjaM.html");
    }






}
