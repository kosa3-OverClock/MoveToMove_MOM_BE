package com.kosa.kosafinalprojbackend.global.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

    RedisTemplate<String, Object> template = new RedisTemplate<>(); // RedisTemplate 생성
    template.setConnectionFactory(factory);                         // RedisConnectionFactory 설정
    template.setKeySerializer(new StringRedisSerializer());         // Redis 키를 문자열로 직렬화
    template.setHashKeySerializer(new StringRedisSerializer());     // Redis 해시 키를 문자열로 직렬화
    template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer()); // Redis 해시 값을 JSON 형식으로 직렬화
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());     // Redis 값을 JSON 형식으로 직렬화
    template.setEnableTransactionSupport(true);                     // Redis 트랜잭션 지원 활성화
    template.afterPropertiesSet();

    return template;
  }

  @Bean
  public RedisTemplate<String, String> sessionRedisTemplate(RedisConnectionFactory factory) {

    RedisTemplate<String, String> template = new RedisTemplate<>(); // RedisTemplate 생성
    template.setConnectionFactory(factory);                         // RedisConnectionFactory 설정
    template.setKeySerializer(new StringRedisSerializer());         // Redis 키를 문자열로 직렬화
    template.setValueSerializer(new StringRedisSerializer());       // Redis 값을 문자열로 직렬화
    template.setEnableTransactionSupport(true);                     // Redis 트랜잭션 지원 활성화
    template.afterPropertiesSet();

    return template;
  }
}
