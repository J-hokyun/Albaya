package com.example.albaya.member.controller;


import com.example.albaya.member.dto.WriteResumeDto;
import com.example.albaya.member.service.MemberService;
import com.example.albaya.user.entity.User;
import com.example.albaya.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {


    private final MemberService memberService;

    @GetMapping("/member/write_resume")
    public String writeResume(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User)principal;
        WriteResumeDto writeResumeDto  = new WriteResumeDto(user);
        model.addAttribute("dto", writeResumeDto);
        return "member/write_resume";

    }

    @PostMapping("/member/write_resume")
    @ResponseBody
    public ResponseEntity<String> writeResume(@RequestBody String json)
    throws JsonProcessingException {
        log.info("Start resume save");
        log.debug("Post Request Write Resume json Content is  : {}", json);
        ObjectMapper mapper = new ObjectMapper();
        WriteResumeDto writeResumeDto = mapper.readValue(json, WriteResumeDto.class);
        memberService.saveMemberResume(writeResumeDto);
       return new ResponseEntity<>("Resume data received successfully!", HttpStatus.OK);
    }
}
