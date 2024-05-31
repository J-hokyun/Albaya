package com.example.albaya.user.service;

import com.example.albaya.config.JwtTokenProvider;
import com.example.albaya.user.dto.TokenDto;
import com.example.albaya.user.dto.UserJoinDto;
import com.example.albaya.user.dto.UserLoginDto;
import com.example.albaya.user.entity.RefreshToken;
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
    public String join (UserJoinDto userJoinDto){
        validateDuplicateUser(userJoinDto.getEmail());
        userJoinDto.setPassword(passwordEncoder.encode(userJoinDto.getPassword()));
        User user = userJoinDto.toEntity();
        userRepository.save(user);
        return user.getEmail();
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

//    public String getRefreshToken(Long id){
//        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email).orElse(null);
//        logger.info("Redis 조회 : " + refreshToken);
//        return refreshToken.getRefreshToken();
//
//    }

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
