package com.kosa.kosafinalprojbackend.domains.kanban.column.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KanbanColumnMoveRequestFoam {
    public Long projectId;
    public int newPosition;
}
