package com.kosa.kosafinalprojbackend.domains.kanban.column.controller;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.KANBAN_CARD_CREATED;
import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.KANBAN_COLUMN_UPDATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.KanbanColumnInCardDto;
import com.kosa.kosafinalprojbackend.domains.kanban.column.model.form.KanbanCardForm;
import com.kosa.kosafinalprojbackend.domains.kanban.column.model.form.KanbanColumnForm;
import com.kosa.kosafinalprojbackend.domains.kanban.column.model.form.KanbanColumnMoveRequestForm;
import com.kosa.kosafinalprojbackend.domains.kanban.column.service.ColumnService;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import com.kosa.kosafinalprojbackend.global.security.model.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kanban-columns")
@RequiredArgsConstructor
public class ColumnController {

    private final ColumnService columnService;

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
    @PatchMapping("/{kanban-column-id}")
    public ResponseEntity<ResponseCode> moveColumn(
            @PathVariable("kanban-column-id") Long kanbanColumnId,
            @RequestBody KanbanColumnMoveRequestForm kanbanColumnMoveRequestForm) {

        // TODO: 칸반 컬럼 이동 로직 처리
        columnService.updateKanbanColumn(kanbanColumnId, kanbanColumnMoveRequestForm);
        // TODO: 칸반 보드의 새로운 상태 반환
        return ResponseEntity.status(OK).body(KANBAN_COLUMN_UPDATE);
    }

    // 칸반 카드 조회 (칸반 컬럼 기준)
    @GetMapping("/{kanban-column-id}/kanban-cards")
    public ResponseEntity<List<KanbanColumnInCardDto>> selectKanbanCardByKanbanColumn(
        @PathVariable("kanban-column-id") Long kanbanColumnId) {

        return new ResponseEntity<>(
            columnService.selectKanbanCardByKanbanColumn(kanbanColumnId), HttpStatus.CREATED);
    }


    // 칸반 카드 생성
    @PostMapping("/{kanban-column-id}/kanban-cards")
    public ResponseEntity<ResponseCode> insertKanbanCard(
        @PathVariable("kanban-column-id") Long kanbanColumnId,
        @RequestBody KanbanCardForm kanbanCardForm,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.status(CREATED)
            .body(KANBAN_CARD_CREATED.withData(
                columnService.insertKanbanCard(kanbanColumnId, kanbanCardForm, customUserDetails.getId())));
    }
}
