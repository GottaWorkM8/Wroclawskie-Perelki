package pl.wroc.projzesp.perelki.wrocperelki.controllers;

import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.wroc.projzesp.perelki.wrocperelki.model.User;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
public class ImageController {
    @Autowired
    private UserController  userController;
    private static int loadSavedFromDisk() {
        try{
            File f = new File("imageId.txt");
            BufferedReader fr = new BufferedReader(new java.io.FileReader(f));
            int id = Integer.parseInt(fr.readLine());
            fr.close();
            return id;
        }
        catch (Exception e){
            return 0;
        }
    }
    private static int imageId = loadSavedFromDisk();
    private static int getNextId(){
        ++imageId;
        try {
            File f = new File("imageId.txt");
            f.createNewFile();
            java.io.FileWriter fw = new java.io.FileWriter(f);
            fw.write(imageId + "");
            fw.close();
            return imageId;
        }
        catch (IOException e){
            return 0;
        }
    }
    @RouterOperation(operation = @Operation(operationId = "upladFile", summary = "wgrywanie pliku do serwera",
            parameters = { @Parameter(in = ParameterIn.PATH, name = "key", description = "Token of logged in user", required = true), },
            responses = { @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid Employee ID supplied"),
                    @ApiResponse(responseCode = "404", description = "Employee not found") }))
    @PostMapping("/api/upload/image")
    public String uplaodImage(@RequestPart("file") MultipartFile file,@RequestParam String key) throws Exception, IOException {
        User u = userController.getUser(key);
        if(u==null)return null;
        //testowanie: curl -F file=@"plik" http://127.0.0.1:8080/api/upload/image
        //powinno zwrócić link do pliku
        int id = getNextId();
        String filename = id+"."+file.getOriginalFilename();
        File imf = new File("/home/pi/projzesp/images/"+filename);
        file.transferTo(imf);
        return "https://szajsjem.mooo.com/images/"+filename;
    }
    @DeleteMapping("/api/image")///https://szajsjem.mooo.comapi/image?link=https://szajsjem.mooo.com/images/nazwapliku
    public String deleteImage(@RequestParam String link,@RequestParam String key) throws Exception {
        User u = userController.getUser(key);
        if(u==null)return null;
        if(!link.startsWith("https://szajsjem.mooo.com/images/")){
            return "Link not valid";
        }
        String filename = link.substring(link.lastIndexOf("/")+1);
        File imf = new File("/home/pi/projzesp/images/"+filename);
        if(imf.delete())
            return "OK";
        return "Image not found";
    }

}
