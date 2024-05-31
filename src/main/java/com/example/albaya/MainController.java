package com.example.albaya;

import com.example.albaya.user.dto.TokenDto;
import com.example.albaya.user.dto.UserInformDto;
import com.example.albaya.user.dto.UserLoginDto;
import com.example.albaya.user.entity.User;
import com.example.albaya.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    private final Logger logger = LoggerFactory.getLogger(MainController.class);
    @GetMapping("/healthcheck")
    public ResponseEntity healthCheck(){
        return new ResponseEntity(HttpStatus.OK);
    }
    @GetMapping("/")
    public String home(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInformDto userInformDto;
        if (principal == "anonymousUser"){
            logger.info("Login 되어 있지 않음");
            userInformDto = UserInformDto.builder()
                    .loginStatus(false)
                    .build();
        }else{
            logger.info("Login 되어 있음");
            User user = (User)principal;
            userInformDto = UserInformDto.builder()
                    .loginStatus(true)
                    .name(user.getName())
                    .build();
        }
        model.addAttribute("informDto", userInformDto);
        return "home";
    }
}
