package com.example.albaya;

import com.example.albaya.config.JwtTokenProvider;
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
@Transactional
public class JwtTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("Create access Token")
    public void createAccessToken(){
        User user = userRepository.findById(1L).orElse(null);
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole().name());

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        Object principal = authentication.getPrincipal();
        User foundUser = (User)principal;

        Assertions.assertEquals(user.getUser_id(), foundUser.getUser_id());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
        Assertions.assertEquals(user.getRole(), foundUser.getRole());
    }
    @Test
    @DisplayName("Create refresh Token")
    public void createRefreshToken() {
        User user = userRepository.findById(1L).orElse(null);
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole().name());
        jwtTokenProvider.createRefreshToken(user.getUser_id(), accessToken);

        RefreshToken foundRefreshToken = refreshTokenRepository.findByAccessToken(accessToken).orElse(null);
        Assertions.assertEquals(user.getUser_id(), foundRefreshToken.getId());
        Assertions.assertEquals(accessToken, foundRefreshToken.getAccessToken());

    }


    @Test
    @DisplayName("Delete refreshToken")
    public void deleteRefreshToken(){
        User user = userRepository.findById(1L).orElse(null);
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole().name());
        jwtTokenProvider.createRefreshToken(user.getUser_id(), accessToken);

        jwtTokenProvider.removeRefreshToken(accessToken);
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken).orElse(null);
        Assertions.assertNull(refreshToken);
    }

    @Test
    @DisplayName("Recreate access Token")
    public void recreateAccessToken(){
        User user = userRepository.findById(1L).orElse(null);
        String originAccessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole().name());
        String refreshToken =  jwtTokenProvider.createRefreshToken(user.getUser_id(), originAccessToken);
        RefreshToken originRefreshToken = refreshTokenRepository.findByAccessToken(originAccessToken).orElse(null);

//
//        String newAccessToken = jwtTokenProvider.reCreateAccessToken(originAccessToken);
//        RefreshToken newRefreshToken = refreshTokenRepository.findByAccessToken(newAccessToken).orElse(null);
//        Assertions.assertEquals(newAccessToken, newRefreshToken.getAccessToken());
    }
}
