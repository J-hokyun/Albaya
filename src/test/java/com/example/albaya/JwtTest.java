package com.example.albaya;

import com.example.albaya.config.JwtTokenProvider;
import com.example.albaya.user.dto.UserJoinDto;
import com.example.albaya.user.entity.RefreshToken;
import com.example.albaya.user.entity.User;
import com.example.albaya.user.repository.RefreshTokenRepository;
import com.example.albaya.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JwtTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;


    @Test
    @DisplayName("Create access Token")
    @Transactional
    public void createAccessToken(){

        UserJoinDto userJoinDto = UserJoinDto.builder()
                .age(1)
                .name("정호균")
                .real_password("Q!1231234")
                .check_password("Q!1231234")
                .email("test1@naver.com")
                .build();

        User saveUser = userRepository.save(userJoinDto.toEntity());
        Long userId = saveUser.getUser_id();

        User user = userRepository.findById(userId).orElse(null);
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole().name());

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        Object principal = authentication.getPrincipal();
        User foundUser = (User)principal;

        Assertions.assertEquals(user.getUser_id(), foundUser.getUser_id());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
        Assertions.assertEquals(user.getRole(), foundUser.getRole());
    }
    @Test
    @DisplayName("Delete refreshToken")
    @Transactional
    public void deleteRefreshToken(){
        UserJoinDto userJoinDto = UserJoinDto.builder()
                .age(1)
                .name("정호균")
                .real_password("Q!1231234")
                .check_password("Q!1231234")
                .email("test1@naver.com")
                .build();

        User saveUser = userRepository.save(userJoinDto.toEntity());
        Long userId = saveUser.getUser_id();

        User user = userRepository.findById(userId).orElse(null);
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole().name());
        jwtTokenProvider.createRefreshToken(user.getUser_id(), accessToken);

        jwtTokenProvider.removeRefreshToken(accessToken);
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken).orElse(null);
        Assertions.assertNull(refreshToken);
    }

    @Test
    @DisplayName("Recreate AccessToken")
    @Transactional
    public void recreateAccessToken(){
        UserJoinDto userJoinDto = UserJoinDto.builder()
                .age(1)
                .name("정호균")
                .real_password("Q!1231234")
                .check_password("Q!1231234")
                .email("test1@naver.com")
                .build();

        User saveUser = userRepository.save(userJoinDto.toEntity());
        Long userId = saveUser.getUser_id();


        User user = userRepository.findById(userId).orElse(null);
        String originAccessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole().name());
        jwtTokenProvider.createRefreshToken(user.getUser_id(), originAccessToken);

        RefreshToken refreshToken = jwtTokenProvider.getRefreshToken(originAccessToken);
        String newAccessToken = jwtTokenProvider.reCreateAccessToken(originAccessToken, refreshToken);

        Assertions.assertEquals(jwtTokenProvider.getUserEmail(originAccessToken), jwtTokenProvider.getUserEmail(newAccessToken));

    }
}
