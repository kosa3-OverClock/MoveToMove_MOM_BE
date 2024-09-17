package com.kosa.kosafinalprojbackend.global.websocket.dto.column;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnAddMessage {
    // 프로젝트 ID
    private Long projectId;
    // 컬럼 ID
    private Long kanbanColumnId;
    // 컬럼명
    private String kanbanColumnName;
    // 컬럼 순서
    private int columnSeq;
    // 메시지 타입
    private String type;
}
