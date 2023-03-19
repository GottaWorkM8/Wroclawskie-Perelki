package com.example.servingwebcontent;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PlacesController {
    @GetMapping("/places")
    public String places(Model model) {
        return "places";
    }
    public String places(@RequestParam(name="q", required=true)int q,Model model) {
        model.addAttribute("q",q);
        return "places";
    }
    @GetMapping("/editplace")
    public String editPlaces(@RequestParam(name="id", required=true) int id, Model model) {
        model.addAttribute("id",id);
        return "editplace";
    }
    @GetMapping("/newplace")
    public String addPlaces(@RequestParam(name="zagadkaid", required=true) int zagadkaid, Model model) {
        model.addAttribute("id",zagadkaid);
        return "addplace";
    }
}
