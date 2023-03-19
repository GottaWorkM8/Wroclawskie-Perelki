package com.example.servingwebcontent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.ExceptionHandler;

@SpringBootApplication
public class ServingWebContentApplication {

    @ExceptionHandler({ Exception.class })
    public void handleException(Exception e) {
        System.out.println(e.getMessage());
    }
    public static void main(String[] args) {
        SpringApplication.run(ServingWebContentApplication.class, args);
    }

}
