package com.kosa.kosafinalprojbackend.domains.kanban.column.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KanbanColumnForm {

    // 프로젝트 ID
    public Long projectId;
    // 컬럼 ID
    public Long kanbanColumnId;
    // 컬럼명
    public String kanbanColumnName;
    // 컬럼 순서
    public int columnSeq;
}
