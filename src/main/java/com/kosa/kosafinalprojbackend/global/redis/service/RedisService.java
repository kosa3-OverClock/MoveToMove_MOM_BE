package com.kosa.kosafinalprojbackend.global.redis.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

  private final RedisTemplate<String, Object> redisTemplate;

  // Refresh 토큰 조회
  public String selectRefreshToken(Long memberId) {
    String key = "member:" + memberId;
    return (String) redisTemplate.opsForHash().get(key, "refreshToken");
  }

  // Refresh 토큰 저장
  public void saveRefreshToken(Long memberId, String refreshToken) {

    String key = "member:" + memberId;
    redisTemplate.opsForHash().put(key, "refreshToken", refreshToken);
    redisTemplate.expire(key, Duration.ofHours(24)); // 세션 만료시간 설정
  }

  // Refresh 토큰 삭제
  public void deleteRefreshToken(Long memberId) {
    redisTemplate.delete("member:" + memberId);
  }


  // 비밀번호 찾기
  // 인증 코드 저장
  public String saveVerificationCode(String email, String code) {
    String key = "verification:" + email;
    redisTemplate.opsForValue().set(key, code, Duration.ofMinutes(2));    // 인증 코드 2분 유효

    // 시간 포맷화
    return getFormattedRemainingTime(key);
  }


  // 시간 포맷화
  private String getFormattedRemainingTime(String key) {
    Long seconds = redisTemplate.getExpire(key, TimeUnit.SECONDS);
    if(seconds == null || seconds <= 0) {
      return "00:00";
    }

    long minutes = seconds / 60;
    long remainingSeconds = seconds % 60;

    return String.format("%02d:%02d", minutes, remainingSeconds);
  }


  // 인증 코드 조회
  public String getVerificationCode(String email) {
    String key = "verification:" + email;
    return (String) redisTemplate.opsForValue().get(key);
  }


  // 인증 코드 삭제
  public void deleteVerificationCode(String email) {
    String key = "verification:" + email;
    redisTemplate.delete(key);
  }

}
