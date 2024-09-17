package com.kosa.kosafinalprojbackend.global.websocket.dto.column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColumnMoveMessage {

    private String columnId; // 이동한 컬럼의 ID
    private int newIndex;    // 컬럼의 새로운 위치
    private String projectId; // 해당 프로젝트 ID
    private String type; // 이벤트 유형
}

