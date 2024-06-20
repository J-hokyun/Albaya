package com.example.albaya;


import com.example.albaya.config.JwtTokenProvider;
import com.example.albaya.exception.CustomException;
import com.example.albaya.exception.StatusCode;
import com.example.albaya.user.dto.TokenDto;
import com.example.albaya.user.dto.UserJoinDto;
import com.example.albaya.user.dto.UserLoginDto;
import com.example.albaya.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserLoginTest {
    @Autowired
    private UserService userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;


    @Test
    @DisplayName("User Login Test")
    public void loginTest(){
        UserJoinDto userJoinDto = UserJoinDto.builder()
                .age(1)
                .name("qwerqwer")
                .real_password("Q!12345678")
                .check_password("Q!12345678")
                .email("test1234@naver.com")
                .build();
        userService.join(userJoinDto);

        UserLoginDto userLoginDto = UserLoginDto.builder()
                .email("test1234@naver.com")
                .password("Q!12345678")
                .build();

        TokenDto tokenDto = userService.login(userLoginDto);
        String email = jwtTokenProvider.getUserEmail(tokenDto.getAccessToken());

        Assertions.assertEquals("test1234@naver.com", email);
    }



    @Test
    @DisplayName("User Login Fail Test_1 (invalid email)")
    public void invalidEmail(){
        UserJoinDto userJoinDto = UserJoinDto.builder()
                .age(1)
                .name("qwerqwer")
                .real_password("Q!12345678")
                .check_password("Q!12345678")
                .email("test1234@naver.com")
                .build();
        userService.join(userJoinDto);

        UserLoginDto userLoginDto = UserLoginDto.builder()
                .email("test123@naver.com")
                .password("Q!12345678")
                .build();

        CustomException exception = Assertions.assertThrows(CustomException.class, () ->{
            userService.login(userLoginDto);
        });
        Assertions.assertEquals(StatusCode.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("User Login Fail Test_2 (invalid password)")
    public void invalidPassword(){
        UserJoinDto userJoinDto = UserJoinDto.builder()
                .age(1)
                .name("qwerqwer")
                .real_password("Q!12345678")
                .check_password("Q!12345678")
                .email("test1234@naver.com")
                .build();
        userService.join(userJoinDto);

        UserLoginDto userLoginDto = UserLoginDto.builder()
                .email("test1234@naver.com")
                .password("Q!12345670")
                .build();

        CustomException exception = Assertions.assertThrows(CustomException.class, () ->{
            userService.login(userLoginDto);
        });
        Assertions.assertEquals(StatusCode.INVALID_PASSWORD, exception.getStatusCode());
    }

}
