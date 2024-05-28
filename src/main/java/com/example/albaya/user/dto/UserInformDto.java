package com.example.albaya.user.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class UserInformDto {
    private boolean loginStatus;
    private String email;
    private String token;
    private String name;
}
