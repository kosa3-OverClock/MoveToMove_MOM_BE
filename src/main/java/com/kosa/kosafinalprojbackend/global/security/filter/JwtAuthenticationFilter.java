package com.kosa.kosafinalprojbackend.global.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  public final String TOKEN_HEADER = "Authorization";
  public final String TOKEN_PREFIX = "Bearer ";
  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {

    String[] api = {"/**" };

    String path = request.getRequestURI();

    return Arrays.stream(api).anyMatch(pattern -> pathMatcher.match(pattern, path));
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try {
      // 토큰
      String token = request.getHeader(TOKEN_HEADER);

      // 토큰이 없을 경우
      if (!StringUtils.hasText(token)) {
        // response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }

      token = token.substring(TOKEN_PREFIX.length());

      if (jwtTokenProvider.validateToken(token, true)) {

        Authentication authentication = jwtTokenProvider.getAuthentication(token, true);

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (ExpiredJwtException ex) {
      // 만료 에러 엑세스 토큰 확인 필요
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    } catch (JwtException ex) {
      // 토큰 변형 에러
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }
    filterChain.doFilter(request, response);
  }
}
