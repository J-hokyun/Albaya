package com.example.albaya.user.dto;


import com.example.albaya.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserInformDto {
    protected boolean loginStatus;
    protected String name;
    protected String role;
    protected String email;

    public UserInformDto(User user) {
        this.loginStatus = true;
        this.name = user.getName();
        this.role = user.getRole();
        this.email = user.getEmail();
    }

    public UserInformDto() {
    }
}
