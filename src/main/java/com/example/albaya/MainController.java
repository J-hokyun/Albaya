package com.example.albaya;

import com.example.albaya.user.dto.TokenDto;
import com.example.albaya.user.dto.UserInformDto;
import com.example.albaya.user.dto.UserLoginDto;
import com.example.albaya.user.entity.User;
import com.example.albaya.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @Value("${naver.map.client-id}")
    private String naverMapClientId;

    private final Logger logger = LoggerFactory.getLogger(MainController.class);
    @GetMapping("/healthcheck")
    public ResponseEntity healthCheck(){
        return new ResponseEntity(HttpStatus.OK);
    }
    @GetMapping("/")
    public String home(@RequestParam(value = "logoutSuccess", required = false)String logoutSuccess, Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInformDto userInformDto;
        if (logoutSuccess != null) {
            model.addAttribute("logoutMessage", "로그아웃 되었습니다.");
        }

        if (principal == "anonymousUser"){
            userInformDto = UserInformDto.builder()
                    .loginStatus(false)
                    .build();
        }else{
            User user = (User)principal;
            userInformDto = UserInformDto.builder()
                    .loginStatus(true)
                    .name(user.getName())
                    .role(user.getRole())
                    .build();
        }

        model.addAttribute("dto", userInformDto);
        model.addAttribute("naverMapClientId", naverMapClientId);
        return "home";
    }
}
