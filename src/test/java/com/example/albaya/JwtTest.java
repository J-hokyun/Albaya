package com.example.albaya;

import com.example.albaya.config.JwtTokenProvider;
import com.example.albaya.user.dto.TokenDto;
import com.example.albaya.user.dto.UserJoinDto;
import com.example.albaya.user.dto.UserLoginDto;
import com.example.albaya.user.entity.RefreshToken;
import com.example.albaya.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class JwtTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserService userService;

    @Test
    @DisplayName("Recreate AccessToken Test")
    public void recreateAccessTokenTest(){
        UserJoinDto userJoinDto = UserJoinDto.builder()
                .age(1)
                .name("qwerqwer")
                .realPassword("Q!12345678")
                .checkPassword("Q!12345678")
                .email("test1234@naver.com")
                .build();
        userService.join(userJoinDto);

        UserLoginDto userLoginDto = UserLoginDto.builder()
                .email("test1234@naver.com")
                .password("Q!12345678")
                .build();

        TokenDto tokenDto = userService.login(userLoginDto);
        RefreshToken refreshToken = jwtTokenProvider.getRefreshToken(tokenDto.getAccessToken());
        String newAccessToken = jwtTokenProvider.reCreateAccessToken(tokenDto.getAccessToken(),refreshToken);

        String emailToNewAccessToken = jwtTokenProvider.getUserEmail(newAccessToken);

        Assertions.assertEquals(userLoginDto.getEmail(), emailToNewAccessToken);
    }
}
