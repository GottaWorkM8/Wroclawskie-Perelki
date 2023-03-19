package com.example.servingwebcontent;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ZagatkaController {

	@ExceptionHandler({ Exception.class })
	public void handleException(Exception e) {
		System.out.println(e.getMessage());
	}
	@GetMapping("/zagadki")
	public String zagadki(Model model) {
		return "zagadki";
	}
	@GetMapping("/edytujZagadke")
	public String edytujZagadke(@RequestParam(name="id", required=true) int id, Model model) {
		model.addAttribute("id", id);
		return "edycjaZ";
	}
	@GetMapping("/dodajZagadke")
	public String nowaZagadka(Model model) {
		return "nowaZ";
	}
//	@GetMapping("/categories")
//	public String categories(Model model) {
//		return "categories";
//	}
//	@GetMapping("/editCategory")
//	public String editCategory(@RequestParam(name="id", required=true) int id, Model model) {
//		model.addAttribute("id", id);
//		return "edycjaC";
//	}
//	@GetMapping("/addCategory")
//	public String addCategory(Model model) {
//		return "nowaC";
//	}



}
