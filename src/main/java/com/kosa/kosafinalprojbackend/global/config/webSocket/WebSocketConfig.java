package com.kosa.kosafinalprojbackend.global.config.webSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
       registry.enableStompBrokerRelay("/topic")    // 메시지 브로커링
               .setRelayHost("localhost")                           // rabbitMQ 호스트 설정
               .setRelayPort(61613);                                // rabbitMQ 포트 설정
       registry.setApplicationDestinationPrefixes("/app");          //
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-kanban")        // 웹 소켓 엔드포인트 설정
                .setAllowedOrigins("*")
                .withSockJS();
    }
}
