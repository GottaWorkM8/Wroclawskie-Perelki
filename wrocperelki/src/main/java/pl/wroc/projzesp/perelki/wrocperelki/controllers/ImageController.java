package pl.wroc.projzesp.perelki.wrocperelki.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
public class ImageController {
    @PostMapping("/upload/image")
    public String uplaodImage(@RequestParam("image") MultipartFile file)
            throws IOException {
//        Image.builder()
//                .name(file.getOriginalFilename())
//                .type(file.getContentType())
//                .image(ImageUtility.compressImage(file.getBytes())).build();
        return "not implemented";
    }
    @PostMapping("/upload/imageinfo")
    public void uplaodImageInfo(Map<String,String> data)
            throws IOException {
    }

}
