package com.kosa.kosafinalprojbackend.global.security.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

  @Value("${cors.front_server_local}")
  private String frontServerCorsLocal;

  @Value("${cors.front_server_vercel}")
  private String frontServerCorsVercel;

  @Value("${cors.front_server_distribute}")
  private String frontServerDistribute;


  
  @Bean
  @Primary
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOriginPattern(frontServerCorsLocal);
    configuration.addAllowedOriginPattern(frontServerCorsVercel);
    configuration.addAllowedOriginPattern(frontServerDistribute);
    // 리디렉션 URI 도메인 추가
    configuration.addAllowedOriginPattern("https://api.move-to-move.online");
    configuration.setAllowedMethods(
        Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    configuration.addAllowedHeader("*");
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
