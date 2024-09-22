package com.kosa.kosafinalprojbackend.global.security.config;

import com.kosa.kosafinalprojbackend.domains.member.controller.CustomLogoutHandler;
import com.kosa.kosafinalprojbackend.domains.member.controller.CustomLogoutSuccessHandler;
import com.kosa.kosafinalprojbackend.domains.member.oAuth.handler.CustomOAuth2AuthenticationFailureHandler;
import com.kosa.kosafinalprojbackend.domains.member.oAuth.handler.CustomOAuth2AuthenticationSuccessHandler;
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
  private final CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;
  private final CustomOAuth2AuthenticationFailureHandler customOAuth2AuthenticationFailureHandler;
  private final CustomLogoutHandler customLogoutHandler;
  private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        // cors 설정
        .cors(cors -> cors.configurationSource(corsConfigurationSource))

        // CSRF 보호 기능을 비활성화
        .csrf(AbstractHttpConfigurer::disable)

        // // 필요한 경우 세션 생성
        .sessionManagement(sessionManagement -> sessionManagement
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

        // 요청 권한 설정
        .authorizeHttpRequests(
            authorizeRequests -> authorizeRequests
                .requestMatchers("/ws/**").permitAll()
                .requestMatchers("/**").permitAll()
                .requestMatchers("/api/oauth2/**", "/api/login/**").permitAll()
                .anyRequest().permitAll()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout -> logout
                    .logoutUrl("/api/members/logout")
                    .addLogoutHandler(customLogoutHandler)
                    .logoutSuccessHandler(customLogoutSuccessHandler)
                    .permitAll()
            );

    // 일반 로그인
//    http.formLogin(httpSecurityFormLoginConfigurer -> {
//            httpSecurityFormLoginConfigurer.loginPage("/**")
//            ;
//        });
//
//    http.httpBasic(AbstractHttpConfigurer::disable);


    // 소셜 로그인
      http.oauth2Login(oauth2 -> oauth2
              .authorizationEndpoint(authConfig ->
                      authConfig.baseUri("/api/oauth2/authorization"))
              .redirectionEndpoint(redirectConfig ->
                      redirectConfig.baseUri("/api/login/oauth2/code/*"))
              .userInfoEndpoint(userInfoEndpointConfig ->
                      userInfoEndpointConfig.userService(customOAuth2UserService))
              .successHandler(customOAuth2AuthenticationSuccessHandler)
              .failureHandler(customOAuth2AuthenticationFailureHandler)
      );

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
