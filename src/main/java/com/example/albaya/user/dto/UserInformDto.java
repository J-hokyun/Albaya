package com.example.albaya.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserInformDto {
    private boolean loginStatus;
    private String email;
    private String name;
    private int age;
    private String token;

    @Override
    public String toString() {
        return "UserInformDto{" +
                "loginStatus=" + loginStatus +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", token='" + token + '\'' +
                '}';
    }
}
