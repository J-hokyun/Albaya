package com.example.albaya.user.controller;

import com.example.albaya.user.dto.TokenDto;
import com.example.albaya.user.dto.UserInformDto;
import com.example.albaya.user.dto.UserLoginDto;
import com.example.albaya.user.entity.User;
import com.example.albaya.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import com.example.albaya.user.dto.UserJoinDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Security;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/join")
    public String userJoin(Model model){
        model.addAttribute("joinDto", new UserJoinDto());
        return "user/join";
    }

    @PostMapping(value = "/join")
    public String userJoin(UserJoinDto joinDto){
        System.out.println(userService.join(joinDto));
        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String userLogin(Model model){
        model.addAttribute("loginDto", new UserLoginDto());
        return "user/login";
    }

    @PostMapping(value = "/login")
    public String userLogin(UserLoginDto loginDto, HttpServletResponse response){
        TokenDto tokenDto = userService.login(loginDto);

        Cookie cookie = new Cookie("Bearer", tokenDto.getAccessToken());
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return "redirect:/";
    }

    /**Exception**/
    @ExceptionHandler(IllegalStateException.class)
    public String IllegalExUser(IllegalStateException e, Model model){
        model.addAttribute("e", e.getMessage());
        return "error/400";

    }
}
