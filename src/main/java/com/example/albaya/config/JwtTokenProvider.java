package com.example.albaya.config;

import com.example.albaya.enums.TokenValid;
import com.example.albaya.user.entity.RefreshToken;
import com.example.albaya.user.entity.User;
import com.example.albaya.user.entity.UserDetails;
import com.example.albaya.user.repository.RefreshTokenRepository;
import com.example.albaya.user.repository.UserRepository;
import com.example.albaya.user.service.UserDetailService;
import com.example.albaya.user.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class JwtTokenProvider {

    private final UserDetailService userDetailService;
    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

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

    public String createRefreshToken(Long id, String accessToken){
        Claims claims = Jwts.claims().setSubject(Long.toString(id));
        Date now = new Date();
        String refreshToken =  Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + refreshTokenExpTime))
                        .signWith(key)
                        .compact();

        refreshTokenRepository.save(new RefreshToken(id, accessToken, refreshToken));
        return refreshToken;
    }

    @Transactional(readOnly = true)
    public RefreshToken getRefreshToken(String accessToken){
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken).orElse(null);
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
        String newAccessToken = createAccessToken(user.getEmail(), user.getRole().name());

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

    public TokenValid validateToken(String token) {
        TokenValid tokenValid;
        try {
           Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
           logger.info("Token is Valid");
           tokenValid = TokenValid.VALID;
        } catch (ExpiredJwtException e) {
            logger.info("Token is TimeOut");
            tokenValid = TokenValid.TIMEOUT;
        } catch (UnsupportedJwtException e) {
            logger.info("Token is Unsupported");
            tokenValid = TokenValid.UNSUPPORTED;
        } catch (Exception e) {
            logger.info("Token Exception");
            tokenValid = TokenValid.EX;
        }
        return tokenValid;
    }

}