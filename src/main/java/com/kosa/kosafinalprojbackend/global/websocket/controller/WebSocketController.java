package com.kosa.kosafinalprojbackend.global.websocket.controller;

import com.kosa.kosafinalprojbackend.global.websocket.dto.CardMoveMessage;
import com.kosa.kosafinalprojbackend.global.websocket.dto.CommonMesssage;
import com.kosa.kosafinalprojbackend.global.websocket.dto.column.ColumnAddMessage;
import com.kosa.kosafinalprojbackend.global.websocket.dto.column.ColumnDeleteMessage;
import com.kosa.kosafinalprojbackend.global.websocket.dto.column.ColumnMoveMessage;
import com.kosa.kosafinalprojbackend.global.websocket.dto.project.ProjectUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate; // WebSocket 메시지 브로드캐스트를 위한 Bean 주입

    // 프로젝트별 드래그 앤 드랍 이벤트 처리
    @MessageMapping("/project/{projectId}/column-move")
    @SendTo("/topic/project/{projectId}")
    public ColumnMoveMessage handleColumnMove(@DestinationVariable String projectId, ColumnMoveMessage message) {
        // 메시지를 그대로 브로드캐스트 (로직이 더 필요할 경우 추가)
        return message;
    }
    // 칸반 카드 이동 - Within Column
    @MessageMapping("/project/{projectId}/card-move-within-column")
    @SendTo("/topic/project/{projectId}")  // 특정 프로젝트의 모든 사용자에게 브로드캐스트
    public CardMoveMessage handleCardMoveWithinColumn(@DestinationVariable String projectId, CardMoveMessage message) {
        return message;
    }

    // 칸반 카드 이동 - Between Column
    @MessageMapping("/project/{projectId}/card-move-between-column")
    @SendTo("/topic/project/{projectId}")
    public CardMoveMessage handleCardMoveBetweenColumn(@DestinationVariable String projectId, CardMoveMessage message) {
        return message;
    }

    // 칸반 컬럼 추가 - Add Column
    @MessageMapping("/project/{projectId}/addColumn")
    @SendTo("/topic/project/{projectId}")
    public ColumnAddMessage addColumn(@DestinationVariable String projectId, ColumnAddMessage message) {
        return message;
    }

    // 칸반 컬럼 삭제 - Delete Column
    @MessageMapping("/project/{projectId}/deleteColumn")
    @SendTo("/topic/project/{projectId}")
    public ColumnDeleteMessage deleteColumn(@DestinationVariable String projectId, ColumnDeleteMessage message) {
        return message;
    }

    // 프로젝트 업데이트
    @MessageMapping("/project/{projectId}/update-project")
    @SendTo("/topic/project/{projectId}")
    public ProjectUpdateMessage updateProject(@DestinationVariable String projectId, ProjectUpdateMessage message) {
        return message;
    }

    // 카드 생성
    @MessageMapping("/project/{projectId}/add-card")
    @SendTo("/topic/project/{projectId}")
    public CommonMesssage addCard(@DestinationVariable String projectId, CommonMesssage message) {
        return message;
    }
}
