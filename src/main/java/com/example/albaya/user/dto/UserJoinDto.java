package com.example.albaya.user.dto;


import com.example.albaya.enums.Role;
import com.example.albaya.user.entity.User;
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

    private String email;
    private String password;
    private String name;
    private int age;

    @Override
    public String toString() {
        return "UserJoinDto{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public User toEntity(){
        return User.builder()
                .age(age)
                .name(name)
                .email(email)
                .password(password)
                .role(Role.USER)
                .created_date(LocalDateTime.now())
                .build();

    }
}
