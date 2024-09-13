package com.kosa.kosafinalprojbackend.global.websocket.controller;

import com.kosa.kosafinalprojbackend.global.websocket.dto.DragEventCardMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate; // WebSocket 메시지 브로드캐스트를 위한 Bean 주입

    // 프로젝트별 드래그 앤 드랍 이벤트 처리
    @MessageMapping("/project/{projectId}/drag")
    public void handleDragEvent(@DestinationVariable String projectId, DragEventCardMessage message) {
        // 받은 메시지 처리 로직
        log.info("Received drag event for project " + projectId + ": " + message);
        // 동일 프로젝트의 다른 클라이언트에게 메시지 브로드캐스트
        messagingTemplate.convertAndSend("/topic/project/" + projectId, message);
    }

    // 추가적인 WebSocket 메시지 처리 메서드를 여기에 추가할 수 있습니다.
}
