package com.kosa.kosafinalprojbackend.domains.member.controller;

import com.kosa.kosafinalprojbackend.global.redis.service.RedisService;
import com.kosa.kosafinalprojbackend.global.security.filter.JwtTokenProvider;
import com.kosa.kosafinalprojbackend.global.security.model.CustomUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {
    // 로그아웃 시 레디스에 저장된 리프레쉬 토큰을 삭제하는 로직 구현
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String accessToken = request.getHeader("Authorization");
        accessToken = accessToken.substring(7);
        log.info("logout access token: {}", accessToken);
        authentication = jwtTokenProvider.getAuthentication(accessToken, true);
        // Authentication 에서 userId를 가지고 와서 레디스에 저장된 토큰 삭제
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
        redisService.deleteRefreshToken(userPrincipal.getId());

        // 쿠키 refresh 토큰 삭제
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);
    }
}
