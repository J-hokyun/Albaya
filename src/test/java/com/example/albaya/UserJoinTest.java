package com.example.albaya;

import com.example.albaya.exception.CustomException;
import com.example.albaya.exception.StatusCode;
import com.example.albaya.user.dto.UserJoinDto;
import com.example.albaya.user.entity.User;
import com.example.albaya.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
public class UserJoinTest {

    @Autowired
    private UserService userService;


    @Test
    @DisplayName("User Valid information Test")
    public void validInformation(){
        UserJoinDto userJoinDto = UserJoinDto.builder()
                .age(1)
                .name("qwerqwer")
                .realPassword("Q!12345678")
                .checkPassword("Q!12345678")
                .email("test1234@naver.com")
                .build();
        userService.join(userJoinDto);

        User user = userService.findUser(userJoinDto.getEmail());
        Assertions.assertNotNull(user);
    }


    @Test
    @DisplayName("User Password Valid Test_1 (short password)")
    public void shortPasswordTest(){
        UserJoinDto userJoinDto = UserJoinDto.builder()
                .age(1)
                .name("qwerqwer")
                .realPassword("Q!34567")
                .checkPassword("Q!34567")
                .email("test1234@naver.com")
                .build();
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            userService.join(userJoinDto);
        });
        Assertions.assertEquals(StatusCode.INVALID_PASSWORD, exception.getStatusCode());
    }

    @Test
    @DisplayName("User Password Valid Test_2 (not match origin and check)")
    public void checkPasswordTest(){
        UserJoinDto userJoinDto = UserJoinDto.builder()
                .age(1)
                .name("qwerqwer")
                .realPassword("Q!12345678")
                .checkPassword("Q!1234567")
                .email("test1234@naver.com")
                .build();
        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            userService.join(userJoinDto);
        });
        Assertions.assertEquals(StatusCode.INVALID_PASSWORD, exception.getStatusCode());
    }


    @Test
    @DisplayName("User Email Valid Test_1 (Invalid email)")
    public void invalidEmail(){

        UserJoinDto userJoinDto = UserJoinDto.builder()
                .age(1)
                .name("qwerqwer")
                .realPassword("Q!12345678")
                .checkPassword("Q!12345678")
                .email("test123naver.com")
                .build();

        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            userService.join(userJoinDto);
        });
        Assertions.assertEquals(StatusCode.INVALID_EMAIL, exception.getStatusCode());
    }

    @Test
    @DisplayName("User Email Valid Test_2 (Duplicate email)")
    public void duplicateEmail(){

        UserJoinDto userJoinDto = UserJoinDto.builder()
                .age(1)
                .name("qwerqwer")
                .realPassword("Q!12345678")
                .checkPassword("Q!12345678")
                .email("test1234@naver.com")
                .build();
        userService.join(userJoinDto);

        CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
            userService.validateEmail("test1234@naver.com");
        });
        Assertions.assertEquals(StatusCode.EMAIL_DUPLICATE, exception.getStatusCode());
    }


}
