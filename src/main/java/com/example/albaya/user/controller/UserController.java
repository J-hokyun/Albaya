package com.example.albaya.user.controller;

import org.springframework.ui.Model;
import com.example.albaya.user.dto.UserJoinDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @GetMapping(value = "/join")
    public String userJoin(Model model){
        model.addAttribute("joinDto", new UserJoinDto());
        return "user/join";
    }

    @PostMapping(value = "/join")
    public String userJoin(UserJoinDto joinDto){
        System.out.println(joinDto);
        return "redirect:/";
    }
}
