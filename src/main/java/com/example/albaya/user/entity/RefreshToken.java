package com.example.albaya.user.entity;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash(value = "token", timeToLive = 604800) // 7일
@NoArgsConstructor
@Getter
@ToString
public class RefreshToken{

    @Id
    private Long id;

    @Indexed
    private String accessToken;
    private String refreshToken;

    public RefreshToken(Long id, String accessToken, String refreshToken){
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
