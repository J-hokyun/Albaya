package com.example.albaya;

import com.example.albaya.config.JwtTokenProvider;
import com.example.albaya.user.dto.TokenDto;
import com.example.albaya.user.dto.UserJoinDto;
import com.example.albaya.user.dto.UserLoginDto;
import com.example.albaya.user.entity.RefreshToken;
import com.example.albaya.user.entity.User;
import com.example.albaya.user.repository.RefreshTokenRepository;
import com.example.albaya.user.repository.UserRepository;
import com.example.albaya.user.service.UserService;
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
    UserService userService;

//    @Test
//    @DisplayName("Recreate AccessToken Test")
//    public void recreateAccessTokenTest(){
//        UserJoinDto userJoinDto = UserJoinDto.builder()
//                .age(1)
//                .name("qwerqwer")
//                .real_password("Q!12345678")
//                .check_password("Q!12345678")
//                .email("test1234@naver.com")
//                .build();
//        userService.join(userJoinDto);
//
//        UserLoginDto userLoginDto = UserLoginDto.builder()
//                .email("test1234@naver.com")
//                .password("Q!12345678")
//                .build();
//
//        TokenDto tokenDto = userService.login_2(userLoginDto);
//        RefreshToken refreshToken = jwtTokenProvider.getRefreshToken(tokenDto.getAccessToken());
//        String newAccessToken = jwtTokenProvider.recreateAccessToken(refreshToken);
//
//        String emailToNewAccessToken = jwtTokenProvider.getUserEmail(newAccessToken);
//
//        Assertions.assertEquals(userLoginDto.getEmail(), emailToNewAccessToken);
//    }

}
