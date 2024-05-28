package com.example.albaya;

import com.example.albaya.user.dto.UserInformDto;
import com.example.albaya.user.entity.User;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/healthcheck")
    public ResponseEntity healthCheck(){
        return new ResponseEntity(HttpStatus.OK);
    }
    @GetMapping("/")
    public String home(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInformDto userInformDto;
        if (principal == "anonymousUser"){
            userInformDto = UserInformDto.builder()
                    .loginStatus(false)
                    .build();
        }else{
            User user = (User)principal;
            userInformDto = UserInformDto.builder()
                    .loginStatus(true)
                    .email(user.getEmail())
                    .name(user.getName())
                    .build();
        }
        model.addAttribute("informDto", userInformDto);
        return "home";
    }
}
