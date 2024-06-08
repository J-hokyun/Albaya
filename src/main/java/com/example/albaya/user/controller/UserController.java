package com.example.albaya.user.controller;

import com.example.albaya.enums.JoinValidStatus;
import com.example.albaya.user.dto.TokenDto;
import com.example.albaya.user.dto.UserInformDto;
import com.example.albaya.user.dto.UserLoginDto;
import com.example.albaya.user.entity.User;
import com.example.albaya.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import com.example.albaya.user.dto.UserJoinDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String userJoin(UserJoinDto joinDto, RedirectAttributes attr){
        JoinValidStatus joinValidStatus = userService.join(joinDto);
        String msg = "";
        if (joinValidStatus == JoinValidStatus.VALID){
            msg = "회원가입이 완료 되었습니다. 로그인 하여 주세요";
            attr.addFlashAttribute("msg", msg);
            return "redirect:/login";
        }else{
            if (joinValidStatus == JoinValidStatus.DUPLICATED_EMAIL){
                msg = "이미 존재하는 이메일 입니다.";
            }else if (joinValidStatus == JoinValidStatus.EMAIL_NOT_VALID) {
                msg = "올바른 이메일을 입력하여 주세요";
            }
            else if (joinValidStatus == JoinValidStatus.PASSWORD_NOT_VALID){
                msg = "비밀번호는 8~16자 영문, 숫자, 특수문자를 하나씩 사용하세요.";
            }else{
                msg = "비밀번호가 일치하지 않습니다.";
            }
            attr.addFlashAttribute("msg", msg);
            return "redirect:/join";
        }
    }

    @GetMapping(value = "/login")
    public String userLogin(Model model){
        model.addAttribute("loginDto", new UserLoginDto());
        return "user/login";
    }

    @PostMapping(value = "/login")
    public String userLogin(UserLoginDto loginDto, HttpServletResponse response, RedirectAttributes attr){
        TokenDto tokenDto = userService.login(loginDto);

        if (tokenDto.getAccessToken() == null){
            String msg = "등록된 아이디가 없거나 비밀번호가 틀립니다.";
            attr.addFlashAttribute("msg", msg);
            return "redirect:/login";
        }else{
            Cookie cookie = new Cookie("Bearer", tokenDto.getAccessToken());
            cookie.setPath("/");
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            return "redirect:/";
        }

    }

    @PostMapping("/emailCheck")
    @ResponseBody
    public int emailCheck(@RequestParam("email") String email){
        return userService.emailCheck(email);
    }

    /**Exception**/
    @ExceptionHandler(IllegalStateException.class)
    public String IllegalExUser(IllegalStateException e, Model model){
        model.addAttribute("e", e.getMessage());
        return "error/400";

    }
}
