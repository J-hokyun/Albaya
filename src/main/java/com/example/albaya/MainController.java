package com.example.albaya;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/healthcheck")
    public ResponseEntity healthCheck(){
        return new ResponseEntity(HttpStatus.OK);
    }
    @GetMapping("/")
    public String home(){
        return "home";
    }
}
