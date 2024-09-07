package com.kosa.kosafinalprojbackend.domains.member.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Value("${frontend.server.url}")
    private String frontendServerUrl;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        response.setStatus(HttpServletResponse.SC_FOUND);  // 302 Found
//        response.setHeader("Location", frontendServerUrl);  //TODO: 리다이렉트 페이지 설정하고 싶으면 + "url"
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"Logout successful\"}");
        response.getWriter().flush();
        //TODO : 쿠키에 존재하는 리프레쉬 토큰 삭제할 수 있는지?
    }
}
