package com.kosa.kosafinalprojbackend.global.security.filter;

import com.kosa.kosafinalprojbackend.global.security.model.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
@Component
public class JwtTokenProvider {

  private final SecretKey accessKey;
  private final SecretKey refreshKey;

  @Value("${access.expire_time}")
  private int ACCESS_EXPIRE_TIME;

  @Value("${refresh.expire_time}")
  private int REFRESH_EXPIRE_TIME;


  // secretKey 암호화
  public JwtTokenProvider(@Value("${access.secret_key}") String accessSecretKey,
      @Value("${refresh.secret_key}") String refreshSecretKey) {


    accessKey = new SecretKeySpec(accessSecretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    refreshKey = new SecretKeySpec(refreshSecretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
  }

  // Access Token
  public String createAccessToken(Long id, String email) {

    return createToken(id, email, ACCESS_EXPIRE_TIME, accessKey);
  }


  // Refresh Token
  public String createRefreshToken(Long id, String email) {

    return createToken(id, email, REFRESH_EXPIRE_TIME, refreshKey);
  }

  // 토큰 생성
  private String createToken(Long id, String email, int expireTime, Key key) {

    // Claims 는 JWT 에서 정보를 저장하고 접근하는 데 사용
    Date now = new Date();
    return Jwts.builder()
        .claim("id", id)
        .claim("email", email)
        .issuedAt(now)
        .expiration(new Date(now.getTime() + expireTime))
        .signWith(key)
        .compact();
  }

  // Claims 의 정보 확인
  private Claims parseClaims(String token, boolean isAccessToken) {

    try {
      SecretKey secretKey = isAccessToken ? accessKey : refreshKey;
      return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    } catch (ExpiredJwtException ex) {
      log.info("만료된 JWT: {}", ex.getMessage());
      throw new ExpiredJwtException(null, null, "ExpiredJwtException");
    } catch (UnsupportedJwtException ex) {
      log.info("지원하지 않는 JWT: {}", ex.getMessage());
      throw new JwtException("UnsupportedJwtException");
    } catch (MalformedJwtException ex) {
      log.info("잘못된 형식의 JWT: {}", ex.getMessage());
      throw new JwtException("MalformedJwtException");
    } catch (SignatureException ex) {
      log.info("서명이 잘못된 JWT: {}", ex.getMessage());
      throw new JwtException("SignatureException");
    } catch (IllegalArgumentException ex) {
      log.info("잘못된 인자가 절단: {}", ex.getMessage());
      throw new JwtException("IllegalArgumentException");
    }
  }

  // 토큰 유효 확인
  public boolean validateToken(String token, boolean isAccessToken) {

    try {
      Claims claims = parseClaims(token, isAccessToken);
      if (claims == null) {
        return false;
      }
      return !claims.getExpiration().before(new Date());
    } catch (ExpiredJwtException ex) {
      throw new ExpiredJwtException(null, null, "ExpiredJwtException");
    } catch (JwtException ex) {
      throw new JwtException(ex.getMessage());
    }
  }

  // 사용자 인증 및 권한 부여
  public Authentication getAuthentication(String token, boolean isAccessToken) {

    Claims claims = parseClaims(token, isAccessToken);

    Long id = Long.parseLong(Objects.requireNonNull(claims).getId());
    String email = Objects.requireNonNull(claims).getSubject();

    CustomUserDetails principal =
        new CustomUserDetails(id, email, "", null);

    // Spring Security 에서 사용자를 인증하는 데 사용
    return new UsernamePasswordAuthenticationToken(principal, null, null);
  }
}
