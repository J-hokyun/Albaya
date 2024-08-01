package com.example.albaya.config;

import com.example.albaya.exception.CustomException;
import com.example.albaya.exception.StatusCode;
import com.example.albaya.user.entity.RefreshToken;
import com.example.albaya.user.entity.User;
import com.example.albaya.user.entity.UserDetails;
import com.example.albaya.user.repository.RefreshTokenRepository;
import com.example.albaya.user.repository.UserRepository;
import com.example.albaya.user.service.UserDetailService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final UserDetailService userDetailService;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    @Value("${spring.jwt.secret}")
    private String secretKey;
    private SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Value("${spring.jwt.access_exp_time}")
    private Long accessTokenExpTime;

    @Value("${spring.jwt.refresh_exp_time}")
    private Long refreshTokenExpTime;

    @PostConstruct
    protected void init() {
        secretKey = Encoders.BASE64.encode(key.getEncoded());
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }


    public String createAccessToken(String email, String role) {
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

    public String createRefreshToken(Long userId, String accessToken){
            log.info("create refreshToken");
            Claims claims = Jwts.claims().setSubject(Long.toString(userId));
            Date now = new Date();
            String refreshToken =  Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + refreshTokenExpTime))
                    .signWith(key)
                    .compact();
            refreshTokenRepository.save(new RefreshToken(userId, accessToken, refreshToken));
        return refreshToken;
    }

    @Transactional(readOnly = true)
    public RefreshToken getRefreshToken(String accessToken){
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken).orElse(null);
        return refreshToken;
    }

    @Transactional(readOnly = true)
    public RefreshToken getRefreshToken(Long userId){
        RefreshToken refreshToken = refreshTokenRepository.findById(userId).orElse(null);
        return refreshToken;
    }

    @Transactional
    public void removeRefreshToken(String accessToken){
        refreshTokenRepository.findByAccessToken(accessToken)
                .ifPresent(refreshToken -> refreshTokenRepository.delete(refreshToken));
    }

    @Transactional
    public String reCreateAccessToken(String originAccessToken, RefreshToken refreshToken){
        Long userId = refreshToken.getId();
        User user = userRepository.findById(userId).orElse(null);
        String newAccessToken = createAccessToken(user.getEmail(), user.getRole());

        removeRefreshToken(originAccessToken);
        refreshTokenRepository.save(new RefreshToken(userId, newAccessToken, refreshToken.getRefreshToken()));
        return newAccessToken;
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

    public String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Bearer".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    public void validateToken(String token){
        if (token == null){
            throw new CustomException(StatusCode.NOT_FOUND);
        }
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new CustomException(StatusCode.TOKEN_EXPIRED);

        } catch (UnsupportedJwtException e) {
            throw new CustomException(StatusCode.TOKEN_UNSUPPORTED);

        } catch (Exception e) {
            throw new CustomException(StatusCode.TOKEN_ERROR);

        }
    }
}