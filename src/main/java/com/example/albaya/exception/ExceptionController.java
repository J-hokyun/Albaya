package com.example.albaya.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@Slf4j
public class ExceptionController {

    @GetMapping("/error-403")
    public String error403(){
        return "/exception/error-403";
    }

    @GetMapping("/error-404")
    public String error404(){
        return "/exception/error-404";
    }
}
