package com.example.albaya;

import com.example.albaya.enums.JoinValidStatus;
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
    @DisplayName("UserJoinDto Valid Test")
    public void userJoinDtoValidTest(){
        UserJoinDto short_password = UserJoinDto.builder()
                .age(1)
                .name("qwerqwer")
                .real_password("Q!34567")
                .check_password("Q!34567")
                .email("test1234@naver.com")
                .build();

        UserJoinDto not_match_password = UserJoinDto.builder()
                .age(1)
                .name("qwerqwer")
                .real_password("Q!12345678")
                .check_password("Q!1234567")
                .email("test1234@naver.com")
                .build();

        UserJoinDto valid_password = UserJoinDto.builder()
                .age(1)
                .name("qwerqwer")
                .real_password("Q!12345678")
                .check_password("Q!12345678")
                .email("test1234@naver.com")
                .build();

        JoinValidStatus shot_password_status = userService.UserJoinValid(short_password);
        JoinValidStatus not_match_password_status = userService.UserJoinValid(not_match_password);
        JoinValidStatus valid_status = userService.UserJoinValid(valid_password);
        Assertions.assertEquals(shot_password_status, JoinValidStatus.PASSWORD_NOT_VALID);
        Assertions.assertEquals(not_match_password_status, JoinValidStatus.PASSWORD_NOT_MATCH);
        Assertions.assertEquals(valid_status, JoinValidStatus.VALID);
    }
    @Test
    @DisplayName("User Login Test")
    public void userJoinTest(){
        UserJoinDto user_1 = UserJoinDto.builder()
                .age(1)
                .name("duplicate")
                .real_password("Q!1231234")
                .check_password("Q!1231234")
                .email("Test@naver.com")
                .build();
        JoinValidStatus  joinValidStatus = userService.join(user_1);

        User newUser = userService.findUser(user_1.getEmail());
        Assertions.assertEquals(joinValidStatus, JoinValidStatus.VALID);
        Assertions.assertNotNull(newUser);
    }

    @Test
    @DisplayName("email Valid Test")
    public void emailValidTest(){

        String email_1 = "Test123@naver.com";
        String email_2 = "testnaver.com";
        String email_3 = "test@navercom";
        String email_4 = "test1@naver.com";

        int result1 = userService.emailCheck(email_1); // 만족하는 이메일
        int result2 = userService.emailCheck(email_2); // 이메일 형태가 X
        int result3 = userService.emailCheck(email_3); // 이메일 형태가 X
        int result4 = userService.emailCheck(email_4); // 이미 존재하는 이메일

        Assertions.assertEquals(result1,0);
        Assertions.assertEquals(result2,1);
        Assertions.assertEquals(result3,1);
        Assertions.assertEquals(result4,2);
    }
    // 0이면 사용가능, 1이면 이메일 형태가 아님, 2이면 이미 사용중인 이메일


    @Test
    @DisplayName("User Login Test")
    public void userLoginTest(){
        UserJoinDto user_1 = UserJoinDto.builder()
                .age(1)
                .name("Login")
                .real_password("Q!1231234")
                .check_password("Q!1231234")
                .email("Test@naver.com")
                .build();
        userService.join(user_1);

        User findUser = userService.findUser("Test@naver.com");
        Assertions.assertEquals(passwordEncoder.matches("Q!1231234", findUser.getPassword()), true);
    }
}
