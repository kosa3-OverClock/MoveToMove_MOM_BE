package com.kosa.kosafinalprojbackend.global.websocket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DragEventCardMessage {
    private Long projectId;
    private Long cardId;
    private int newIndex;
}
