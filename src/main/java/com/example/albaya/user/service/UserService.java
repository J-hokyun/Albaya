package com.example.albaya.user.service;

import com.example.albaya.user.dto.UserJoinDto;
import com.example.albaya.user.entity.User;
import com.example.albaya.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    @Transactional
    public String join (UserJoinDto userJoinDto){
        validateDuplicateUser(userJoinDto.getEmail());
        User user = userJoinDto.toEntity();
        userRepository.save(user);
        return user.getEmail();
    }

    public User findUser(String email){
        return userRepository.findByEmail(email).orElse(null);
    }


    /**검증 로직**/
    private void validateDuplicateUser(String email){
        User findUser = userRepository.findByEmail(email).orElse(null);
        if (findUser != null){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

}
