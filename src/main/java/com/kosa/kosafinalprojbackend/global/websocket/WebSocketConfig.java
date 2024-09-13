package com.kosa.kosafinalprojbackend.global.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${cors.front_server_local}")
    private String frontServerCorsLocal;

    @Value("${cors.front_server_vercel}")
    private String frontServerCorsVercel;


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        // 메시지 브로커 설정
        config.enableSimpleBroker("/topic", "/queue"); // 클라이언트가 구독할 수 있는 엔드포인트 설정
        config.setApplicationDestinationPrefixes("/app"); // 클라이언트가 메시지를 전송할 때 사용할 엔드포인트 프리픽스
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // 클라이언트가 연결할 WebSocket 엔드포인트
            .setAllowedOrigins(frontServerCorsLocal, frontServerCorsVercel) // CORS 설정
//            .setAllowedOrigins(frontServerCorsVercel) // CORS 설정
            .withSockJS()
            .setHeartbeatTime(15000); // SockJS 지원
    }
}
