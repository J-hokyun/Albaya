package com.example.albaya.user.service;

import com.example.albaya.config.JwtTokenProvider;
import com.example.albaya.enums.JoinValidStatus;
import com.example.albaya.user.dto.TokenDto;
import com.example.albaya.user.dto.UserJoinDto;
import com.example.albaya.user.dto.UserLoginDto;
import com.example.albaya.user.entity.User;
import com.example.albaya.user.repository.RefreshTokenRepository;
import com.example.albaya.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public JoinValidStatus join (UserJoinDto userJoinDto){
        JoinValidStatus joinValidStatus = UserJoinValid(userJoinDto);
        if (joinValidStatus == JoinValidStatus.VALID){
            userJoinDto.setReal_password(passwordEncoder.encode(userJoinDto.getReal_password()));
            User user = userJoinDto.toEntity();
            userRepository.save(user);
        }
        return joinValidStatus;
    }

    @Transactional
    public TokenDto login(UserLoginDto userLoginDto){
        User findUser = userRepository.findByEmail(userLoginDto.getEmail()).orElse(null);
        TokenDto tokenDto;
        if (findUser != null && passwordEncoder.matches(userLoginDto.getPassword(), findUser.getPassword())){
                tokenDto = TokenDto.builder()
                     .accessToken(jwtTokenProvider.createAccessToken(findUser.getEmail(), findUser.getRole().name()))
                     .build();
                String refreshToken = jwtTokenProvider.createRefreshToken(findUser.getUser_id(), tokenDto.getAccessToken());
                logger.info("email : " + findUser.getEmail() + "Login Success");
                logger.info("accessToken : " + tokenDto.getAccessToken());
                logger.info("refreshToken : " + refreshToken);
        }else{
             logger.info("email : " + userLoginDto.getEmail() + "Login Failed");
             tokenDto = TokenDto.builder().build();
        }
        return tokenDto;
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

    public JoinValidStatus UserJoinValid(UserJoinDto userJoinDto){
        JoinValidStatus joinValidStatus;
        int email_valid = emailCheck(userJoinDto.getEmail());

        if (email_valid == 1){
            joinValidStatus = JoinValidStatus.EMAIL_NOT_VALID;
        } else if (email_valid == 2) {
            joinValidStatus = JoinValidStatus.DUPLICATED_EMAIL;
        } else if(!userJoinDto.getReal_password().equals(userJoinDto.getCheck_password())){
            joinValidStatus = JoinValidStatus.PASSWORD_NOT_MATCH;
        }else if (!passwordCheck(userJoinDto.getReal_password())){
            joinValidStatus = JoinValidStatus.PASSWORD_NOT_VALID;
        }else{
            joinValidStatus = JoinValidStatus.VALID;
        }
        return joinValidStatus;
    }

    private boolean passwordCheck(String password){
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);

        if (!matcher.matches()){
            return false;
        }
        return true;
    }

    public int emailCheck(String email){
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            return 1; // Invalid email format
        }

        boolean result = userRepository.existsByEmail(email);
        if (result) {
            return 2; // Email already exists
        }
        return 0;
    }
}
