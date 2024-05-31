package com.example.albaya.user.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@ToString
public class TokenDto {
    private String accessToken;
}
