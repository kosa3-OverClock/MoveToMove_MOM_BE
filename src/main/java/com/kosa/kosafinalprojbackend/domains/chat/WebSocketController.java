package com.kosa.kosafinalprojbackend.domains.chat;

import com.kosa.kosafinalprojbackend.domains.chat.model.WebSocketMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // 클라이언트가 /app/chat.sendMessage로 보낸 메시지를 처리
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public WebSocketMessage sendMessage(WebSocketMessage message) {
        String processedContent = HtmlUtils.htmlEscape(message.getContent());
        return new WebSocketMessage(processedContent, null);
    }

    // 클라이언트가 특정 유저에게 메시지를 보낼 때
    @MessageMapping("/chat.sendPrivateMessage")
    public void sendPrivateMessage(WebSocketMessage message) {
        String recipient = message.getRecipient();
        String processedContent = HtmlUtils.htmlEscape(message.getContent());

        // 특정 유저의 개인 큐로 메시지를 전송
        messagingTemplate.convertAndSendToUser(recipient, "/queue/private", new WebSocketMessage(processedContent, null));
    }
}
