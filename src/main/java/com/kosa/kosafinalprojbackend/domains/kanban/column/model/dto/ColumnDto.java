package com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnDto {

    private Long kanbanColumnId;        //칸반 컬럼  PK
    private String kanbanColumnName;    //칸반 컬럼 이름
    private int columnSeq;     //칸반 컬럼 순서
}
