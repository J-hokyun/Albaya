package com.example.albaya.config;

import com.example.albaya.enums.TokenValid;
import com.example.albaya.user.entity.RefreshToken;
import com.example.albaya.user.entity.User;
import com.example.albaya.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("AccessToken 검증 시작");
        String accessToken = jwtTokenProvider.resolveToken(request);
        TokenValid accessTokenValid = jwtTokenProvider.validateToken(accessToken);
        if (accessTokenValid == TokenValid.VALID){
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else if (accessTokenValid == TokenValid.TIMEOUT){
            logger.info("Start recreate AccessToken");
            RefreshToken refreshToken = jwtTokenProvider.getRefreshToken(accessToken);
            if (jwtTokenProvider.validateToken(refreshToken.getRefreshToken()) == TokenValid.VALID){
                String newAccessToken = jwtTokenProvider.reCreateAccessToken(accessToken, refreshToken);
                System.out.println(newAccessToken);

                Cookie cookie = new Cookie("Bearer", newAccessToken);
                cookie.setPath("/");
                cookie.setSecure(true);
                cookie.setHttpOnly(true);
                response.addCookie(cookie);

                Authentication authentication = jwtTokenProvider.getAuthentication(newAccessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
