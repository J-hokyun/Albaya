package com.example.albaya.user.service;

import com.example.albaya.user.dto.UserInformDto;
import com.example.albaya.user.dto.UserJoinDto;
import com.example.albaya.user.dto.UserLoginDto;
import com.example.albaya.user.entity.User;
import com.example.albaya.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String join (UserJoinDto userJoinDto){
        validateDuplicateUser(userJoinDto.getEmail());
        userJoinDto.setPassword(passwordEncoder.encode(userJoinDto.getPassword()));
        User user = userJoinDto.toEntity();
        userRepository.save(user);
        return user.getEmail();
    }

    @Transactional
    public UserInformDto login(UserLoginDto userLoginDto){
        User findUser = userRepository.findByEmail(userLoginDto.getEmail()).orElse(null);
        UserInformDto userInformDto;
        if (findUser != null && passwordEncoder.matches(userLoginDto.getPassword(), findUser.getPassword())){
            // JWT token 발행 로직 추가
             userInformDto = UserInformDto.builder()
                    .loginStatus(true)
                    .email(findUser.getEmail())
                    .name(findUser.getName())
                    .age(findUser.getAge())
                    .token("qwerqwer")
                    .build();
        }else{
             userInformDto = UserInformDto.builder()
                    .loginStatus(false)
                    .build();
        }

        return userInformDto;
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
