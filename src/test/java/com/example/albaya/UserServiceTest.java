package com.example.albaya;

import com.example.albaya.user.dto.UserJoinDto;
import com.example.albaya.user.entity.User;
import com.example.albaya.user.repository.UserRepository;
import com.example.albaya.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("User Login Test")
    public void userJoinTest(){
        UserJoinDto user_1 = UserJoinDto.builder()
                .age(1)
                .name("duplicate")
                .password("1234")
                .email("Test@naver.com")
                .build();
        userService.join(user_1);

        User newUser = userService.findUser(user_1.getEmail());
        Assertions.assertNotNull(newUser, "new user is not null");
    }

    @Test
    @DisplayName("Email Duplicate Test")
    public void emailDuplicateTest(){
        UserJoinDto user_1 = UserJoinDto.builder()
                .age(1)
                .name("duplicate")
                .password("1234")
                .email("Test@naver.com")
                .build();
        userService.join(user_1);

        UserJoinDto user_2 = UserJoinDto.builder()
                .age(1)
                .name("duplicate")
                .password("1234")
                .email("Test@naver.com")
                .build();
        Assertions.assertThrows(IllegalStateException.class, () -> {
            userService.join(user_2);
        });
    }

    @Test
    @DisplayName("User Login Test")
    public void userLoginTest(){
        UserJoinDto user_1 = UserJoinDto.builder()
                .age(1)
                .name("Login")
                .password("aq123ftfa")
                .email("Test@naver.com")
                .build();
        userService.join(user_1);

        User findUser = userService.findUser("Test@naver.com");
        Assertions.assertEquals(passwordEncoder.matches("aq123ftfa", findUser.getPassword()), true);
    }
}
