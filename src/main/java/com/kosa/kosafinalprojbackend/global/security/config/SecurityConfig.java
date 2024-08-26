package com.kosa.kosafinalprojbackend.global.security.config;

import com.kosa.kosafinalprojbackend.domains.member.oAuth.service.CustomOAuth2UserService;
import com.kosa.kosafinalprojbackend.global.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CorsConfigurationSource corsConfigurationSource;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomOAuth2UserService customOAuth2UserService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        // cors 설정
        .cors(cors -> cors.configurationSource(corsConfigurationSource))

        // CSRF 보호 기능을 비활성화
        .csrf(AbstractHttpConfigurer::disable)

        // 세션 관리 비활성화
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS))

        // 요청 권한 설정
        .authorizeHttpRequests(
            authorizeRequests -> authorizeRequests
                .requestMatchers("/**").permitAll()
                .anyRequest().permitAll()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    // 일반 로그인
//    http.formLogin(httpSecurityFormLoginConfigurer -> {
//            httpSecurityFormLoginConfigurer.loginPage("/**")
//            ;
//        });
//
//    http.httpBasic(AbstractHttpConfigurer::disable);


    // 소셜 로그인
      http.oauth2Login(oauth2 -> oauth2
              .userInfoEndpoint(userInfoEndpointConfig ->
                      userInfoEndpointConfig.userService(customOAuth2UserService))
      );

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
