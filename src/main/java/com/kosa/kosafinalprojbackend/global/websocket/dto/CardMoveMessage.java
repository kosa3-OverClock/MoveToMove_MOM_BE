package com.kosa.kosafinalprojbackend.global.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardMoveMessage {
    private Long projectId;
    private Long cardId;
    private Long columnId;
    private int newIndex;
    private String type;
}
