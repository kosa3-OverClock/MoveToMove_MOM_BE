package com.kosa.kosafinalprojbackend.domains.member.oAuth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import com.kosa.kosafinalprojbackend.global.error.exception.OAuth2AuthorizationRequestCancelledException;
import com.kosa.kosafinalprojbackend.global.error.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomOAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Value("${frontend.server.url}")
    private String frontendServerUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("소셜 로그인 exception 내용: {}", exception.getMessage());
        // 예외에 따라 ResponseCode를 결정합니다.
        ResponseCode responseCode = determineResponseCode(exception);

        // 에러 응답을 JSON으로 작성합니다.
        ErrorResponse errorResponse = ErrorResponse.of(responseCode);

        // HTTP 상태 설정
        response.setStatus(responseCode.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        // 에러 메시지를 JSON으로 변환하여 응답에 씁니다.
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));

        // TODO: front 서버로 리다에릭트 해야함.
        // 프론트엔드 서버로 리다이렉트 URL을 생성합니다.
        String redirectUrl = String.format("%s/login?error=%s", frontendServerUrl, responseCode.getStatus());

        // 프론트 서버로 리다이렉트 합니다.
        response.sendRedirect(redirectUrl);
    }

    private ResponseCode determineResponseCode(AuthenticationException exception) {
        if (exception instanceof OAuth2AuthenticationException) {
            // OAuth2 인증 관련 에러 처리
            String errorCode = ((OAuth2AuthenticationException) exception).getError().getErrorCode();
            if (errorCode.equals("invalid_grant")) {
                return ResponseCode.OAUTH2_INVALID_GRANT;
            } else if (errorCode.equals("invalid_token")) {
                return ResponseCode.OAUTH2_TOKEN_EXPIRED;
            } else {
                return ResponseCode.OAUTH2_AUTHENTICATION_ERROR;
            }
        } else if (exception instanceof OAuth2AuthorizationRequestCancelledException) {
            // 사용자가 로그인 요청을 취소한 경우 처리
            return ResponseCode.OAUTH2_LOGIN_CANCELLED;
        }
        else {
            // 기타 예외 처리
            return ResponseCode.OAUTH2_PROVIDER_ERROR;
        }
    }
}
