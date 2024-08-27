package com.kosa.kosafinalprojbackend.global.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

  private final RedisTemplate<String, Object> redisTemplate;

  // Refresh 토큰 조회
  public String selectRefreshToken(Long userId) {
    String key = "member:" + userId;
    return (String) redisTemplate.opsForHash().get(key, "refreshToken");
  }

  // Refresh 토큰 저장
  public void saveRefreshToken(Long userId, String refreshToken) {

    String key = "member:" + userId;
    redisTemplate.opsForHash().put(key, "refreshToken", refreshToken);
    redisTemplate.expire(key, Duration.ofHours(24)); // 세션 만료시간 설정
  }

  // Refresh 토큰 삭제
  public void deleteRefreshToken(Long userId) {
    redisTemplate.delete("member:" + userId);
  }
}
