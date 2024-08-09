package com.example.albaya.member.service;


import com.example.albaya.member.dto.WriteResumeDto;
import com.example.albaya.member.entity.Resume;
import com.example.albaya.member.repository.ResumeRepository;
import com.example.albaya.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MemberService {

    @Autowired
    private ResumeRepository resumeRepository;
    @Transactional
    public Long saveMemberResume(WriteResumeDto writeResumeDto){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User)principal;
        Resume resume = writeResumeDto.toEntity(user);
        log.debug("user_id : {}, resume information : {}", user.getUserId(), resume);
        return resumeRepository.save(resume).getResumeId();
    }
}
