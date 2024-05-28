package com.example.albaya.config;

import com.example.albaya.enums.Role;
import com.example.albaya.user.dto.UserInformDto;
import com.example.albaya.user.entity.User;
import com.example.albaya.user.entity.UserDetails;
import com.example.albaya.user.service.UserDetailService;
import com.example.albaya.user.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailService userDetailService;

    @Value("${spring.jwt.secret}")
    private String secretKey;
    private SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Value("${spring.jwt.expiration_time}")
    private Long accessTokenExpTime;

    @PostConstruct
    protected void init() {
        secretKey = Encoders.BASE64.encode(key.getEncoded());
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }


    public String createToken(String email, String role) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims) //데이터
                .setIssuedAt(now) // 토큰 발행 일자
                .setExpiration(new Date(now.getTime() + accessTokenExpTime)) //만료 기간
                .signWith(key) //secret 값
                .compact(); // 토큰 생성

        return token;
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailService.getUser(this.getUserEmail(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserEmail(String token) {
        String info = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
        return info;
    }

    public String resolveToken(HttpServletRequest request)
    {
        String token = new String();
        if(request.getCookies()==null)
        {
            token = null;
        }
        else {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("X-AUTH-TOKEN"))
                {
                    token = cookie.getValue();
                }
            }
        }
        return token;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return false;
        } catch (UnsupportedJwtException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}