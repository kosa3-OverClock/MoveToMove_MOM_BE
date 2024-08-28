package com.kosa.kosafinalprojbackend.domains.member.oAuth.handler;

import com.kosa.kosafinalprojbackend.domains.member.oAuth.domain.Member;
import com.kosa.kosafinalprojbackend.domains.member.oAuth.dto.info.UserPrincipal;
import com.kosa.kosafinalprojbackend.global.redis.service.RedisService;
import com.kosa.kosafinalprojbackend.global.security.filter.JwtTokenProvider;
import com.kosa.kosafinalprojbackend.mybatis.mappers.member.MemberOAuthMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    @Value("${frontend.server.url}")
    private String frontendServerUrl;
    /*
    *   소셜 아이디로 로그인에 성공했을 경우 jwt를 생성하고 레디스에 토큰을 저장 후 프론트 서버로 토큰을 전달하는 메서드
    * */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Member loginUser = userPrincipal.getMember();
        log.info("로그인한 userDetail = {}", loginUser.toString());

        // 회원정보를 기반으로 JWT 토큰 생성하기
        String accessToken = jwtTokenProvider.createAccessToken(loginUser.getMemberId(), loginUser.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(loginUser.getMemberId(), loginUser.getEmail());
        log.info("Generated access token = {}", accessToken);
        log.info("Generated refresh token = {}", refreshToken);
        // Redis에 JWT 토큰 저장
        redisService.saveRefreshToken(loginUser.getMemberId(), refreshToken);
        // 응답 설정

    }
}
