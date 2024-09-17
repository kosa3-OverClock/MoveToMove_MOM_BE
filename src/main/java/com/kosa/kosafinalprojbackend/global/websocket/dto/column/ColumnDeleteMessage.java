package com.kosa.kosafinalprojbackend.global.websocket.dto.column;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnDeleteMessage {

    private Long projectId;
    private Long KanbanColumnId;
    private String type;
}
