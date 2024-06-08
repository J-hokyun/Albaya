package com.example.albaya.user.dto;


import com.example.albaya.enums.Role;
import com.example.albaya.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJoinDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String real_password;

    private String check_password;


    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "나이를 입력해주세요.")
    private int age;


    @Override
    public String toString() {
        return "UserJoinDto{" +
                "email='" + email + '\'' +
                ", real_password='" + real_password + '\'' +
                ", check_password='" + check_password + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public User toEntity(){
        return User.builder()
                .age(age)
                .name(name)
                .email(email)
                .password(real_password)
                .role(Role.USER)
                .created_date(LocalDateTime.now())
                .build();

    }
}
