package com.example.albaya.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserInformDto {
    private boolean loginStatus;
    private String name;
    private String role;
}
