package com.example.albaya.user.controller;

import com.example.albaya.exception.CustomException;
import com.example.albaya.exception.StatusCode;
import com.example.albaya.user.dto.*;
import com.example.albaya.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/join")
    public String userJoin(Model model) {
        model.addAttribute("joinDto", new UserJoinDto());
        return "user/join";
    }

    @PostMapping("/join")
    public String userJoin(UserJoinDto joinDto, RedirectAttributes attr) {
        try {
            userService.join(joinDto);
            attr.addFlashAttribute("msg", "회원가입이 완료 되었습니다. 로그인 하여 주세요");
            return "redirect:/login";
        } catch (CustomException ex) {
            String msg = "";
            if (ex.getStatusCode() == StatusCode.EMAIL_DUPLICATE) {
                msg = "이미 존재하는 이메일 입니다.";
            } else if (ex.getStatusCode() == StatusCode.INVALID_EMAIL) {
                msg = "올바른 이메일을 입력하여 주세요";
            } else if (ex.getStatusCode() == StatusCode.INVALID_PASSWORD) {
                msg = "비밀번호는 8~16자 영문, 숫자, 특수문자를 하나씩 사용하세요.";
            } else {
                msg = "회원가입 처리 중 오류가 발생했습니다.";
            }
            attr.addFlashAttribute("msg", msg);
            return "redirect:/join";
        }
    }
    @GetMapping(value = "/login")
    public String userLogin(Model model) {
        model.addAttribute("loginDto", new UserLoginDto());
        return "user/login";
    }

    @GetMapping(value = "/expired")
    public String expired(RedirectAttributes attr, Model model){
        log.info("expired controller");
        String msg_2 = "세션이 만료되었습니다. 다시 로그인 하여 주세요";
        model.addAttribute("loginDto", new UserLoginDto());
        attr.addFlashAttribute("msg_2", msg_2);
        return "user/login";
    }

    @PostMapping(value = "/login")
    public String userLogin(UserLoginDto loginDto, HttpServletResponse response, RedirectAttributes attr) {
        try{
            TokenDto tokenDto =  userService.login(loginDto);
            Cookie cookie = new Cookie("Bearer", tokenDto.getAccessToken());
            cookie.setPath("/");
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            return "redirect:/";
        }catch (CustomException loginEx){
            String msg = "등록된 아이디가 없거나 비밀번호가 틀립니다.";
            attr.addFlashAttribute("msg", msg);
            return "redirect:/login";
        }
    }

    @PostMapping("/emailCheck")
    @ResponseBody
    public int emailCheck(@RequestParam("email") String email) {
        try {
            userService.validateEmail(email);
            return 0;
        } catch (CustomException ex) {
            if (ex.getStatusCode() == StatusCode.EMAIL_DUPLICATE) {
                return 1;
            }else{
                return 2;
            }
        }
    }

    /**
     * Exception
     **/
    @ExceptionHandler(IllegalStateException.class)
    public String IllegalExUser(IllegalStateException e, Model model) {
        model.addAttribute("e", e.getMessage());
        return "error/400";

    }
}
