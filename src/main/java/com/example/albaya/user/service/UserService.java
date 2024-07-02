package com.example.albaya.user.service;

import com.example.albaya.config.JwtTokenProvider;
import com.example.albaya.exception.CustomException;
import com.example.albaya.exception.StatusCode;
import com.example.albaya.user.dto.TokenDto;
import com.example.albaya.user.dto.UserJoinDto;
import com.example.albaya.user.dto.UserLoginDto;
import com.example.albaya.user.entity.User;
import com.example.albaya.user.repository.RefreshTokenRepository;
import com.example.albaya.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void join (UserJoinDto userJoinDto){
        log.info("User Join Service : {}", userJoinDto);
        validateEmail(userJoinDto.getEmail());
        validatePassword(userJoinDto.getReal_password(), userJoinDto.getCheck_password());

        userJoinDto.setReal_password(passwordEncoder.encode(userJoinDto.getReal_password()));
        User user = userJoinDto.toEntity();
        userRepository.save(user);

    }
    public TokenDto login(UserLoginDto userLoginDto){
        User findUser = userRepository.findByEmail(userLoginDto.getEmail()).orElse(null);
        log.info("Start User Login");
        if (findUser == null){
            log.error("USER_EMAIL_NOT_FOUND");
            throw new CustomException(StatusCode.NOT_FOUND);
        }
        if (!passwordEncoder.matches(userLoginDto.getPassword(), findUser.getPassword())){
            log.error("USER_PASSWORD_MISMATCH");
            throw new CustomException(StatusCode.INVALID_PASSWORD);
        }

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(findUser.getEmail(), findUser.getRole().name()))
                .build();
        jwtTokenProvider.createRefreshToken(findUser.getUser_id(), tokenDto.getAccessToken());
        return tokenDto;
    }

    public User findUser(String email){
        return userRepository.findByEmail(email).orElse(null);
    }


    public User findUser(Long userId){
        return userRepository.findById(userId).orElse(null);
    }





    /**검증 로직**/
    private void validatePassword(String originPassword, String checkPassword){
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(originPassword);

        if (!matcher.matches()){
            log.error("INVALID_PASSWORD Exception");
            throw new CustomException(StatusCode.INVALID_PASSWORD);
        }else if(!originPassword.equals(checkPassword)){
            log.error("INVALID_PASSWORD Exception");
            throw new CustomException(StatusCode.INVALID_PASSWORD);
        }
    }

    public void validateEmail(String email){
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            log.error("INVALID_EMAIL Exception");
            throw new CustomException(StatusCode.INVALID_EMAIL);
        }

        boolean result = userRepository.existsByEmail(email);
        if (result) {
            log.error("EMAIL_DUPLICATE Exception");
            throw new CustomException(StatusCode.EMAIL_DUPLICATE);
        }
    }
}
