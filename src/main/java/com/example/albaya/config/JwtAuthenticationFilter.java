package com.example.albaya.config;

import com.example.albaya.exception.CustomException;
import com.example.albaya.exception.StatusCode;
import com.example.albaya.user.entity.RefreshToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.resolveToken(request);
        try{
            jwtTokenProvider.validateToken(accessToken);
            log.info("accessToken is Valid");
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (CustomException accessTokenEx){
            if (accessTokenEx.getStatusCode() == StatusCode.TOKEN_EXPIRED) {
                log.info("accessToken is timeout");
                try {
                    RefreshToken refreshToken = jwtTokenProvider.getRefreshToken(accessToken);
                    jwtTokenProvider.validateToken(refreshToken.getRefreshToken());

                    String newAccessToken = jwtTokenProvider.reCreateAccessToken(accessToken, refreshToken);
                    logger.info("recreate  accessToken is succeed");
                    Cookie cookie = new Cookie("Bearer", newAccessToken);
                    cookie.setPath("/");
                    cookie.setSecure(true);
                    cookie.setHttpOnly(true);
                    response.addCookie(cookie);

                    Authentication authentication = jwtTokenProvider.getAuthentication(newAccessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } catch (CustomException refreshTokenEx) {
                    log.error("refreshToken is timeout or error user should login again");
                    jwtTokenProvider.removeRefreshToken(accessToken);
                    response.sendRedirect("/logout");
                    return;
                }
            }else if(accessTokenEx.getStatusCode() == StatusCode.NOT_FOUND){
                log.info("user before login");
            }else{
                log.info("token error");
                jwtTokenProvider.removeRefreshToken(accessToken);
                response.sendRedirect("/logout");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}