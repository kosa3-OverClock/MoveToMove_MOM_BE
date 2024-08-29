package com.kosa.kosafinalprojbackend.domains.kanban.column.controller;

import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.ColumnDto;
import com.kosa.kosafinalprojbackend.domains.kanban.column.model.form.KanbanColumnForm;
import com.kosa.kosafinalprojbackend.domains.kanban.column.service.ColumnService;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static javax.security.auth.callback.ConfirmationCallback.OK;

@RestController
@RequestMapping("/api/kanban-columns")
@RequiredArgsConstructor
public class ColumnController {

    private final ColumnService columnService;
    // 칸반 컬럼 조회
    @GetMapping("/{project-id}")
    public ResponseEntity<List<ColumnDto>> getAllColumnsByProjectId(@PathVariable("project-id") Long projectId) {

        return new ResponseEntity<>(columnService.selectColumn(projectId), HttpStatus.OK);
    }
    // 칸반 컬럼 삭제
    @DeleteMapping("/{kanban-column-id}")
    public ResponseEntity<ResponseCode> deleteColumn(@PathVariable("kanban-column-id") Long kanbanColumnId) {
        columnService.deleteColumn(kanbanColumnId);
        return ResponseEntity.ok(ResponseCode.COLUMN_DELETE);
    }

    // 칸반 컬럼 삽입, 수정 (프로젝트 ID, 컬럼 ID, 컬럼명, 컬럼 순서)
    @PatchMapping("")
    public ResponseEntity<ResponseCode> updateColumns(@RequestBody List<KanbanColumnForm> kanbanColumnForms) {
        columnService.upsertColumn(kanbanColumnForms);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseCode.KANBAN_COLUMN_UPDATE);
    }

    // 칸반 컬럼 순서 수정
}
