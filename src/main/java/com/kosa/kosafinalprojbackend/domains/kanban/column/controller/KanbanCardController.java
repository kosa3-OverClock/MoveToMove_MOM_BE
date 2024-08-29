package com.kosa.kosafinalprojbackend.domains.kanban.column.controller;

import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.KanbanColumnInCardDto;
import com.kosa.kosafinalprojbackend.domains.kanban.column.model.form.KanbanCardForm;
import com.kosa.kosafinalprojbackend.domains.kanban.column.service.KanbanCardService;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import com.kosa.kosafinalprojbackend.global.security.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.KANBAN_CARD_CREATED;
import static org.springframework.http.HttpStatus.CREATED;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/kanban-columns")
public class KanbanCardController {

  private final KanbanCardService kanbanCardService;

  // 칸반 카드 조회 (칸반 컬럼 기준)
  @GetMapping("/{kanban-column-id}/kanban-cards")
  public ResponseEntity<List<KanbanColumnInCardDto>> selectKanbanCardByKanbanColumn(
      @PathVariable("kanban-column-id") Long kanbanColumnId) {

    return new ResponseEntity<>(
        kanbanCardService.selectKanbanCardByKanbanColumn(kanbanColumnId), HttpStatus.CREATED);
  }

  // 칸반 카드 생성
  @PostMapping("/{kanban-column-id}/kanban-cards")
  public ResponseEntity<ResponseCode> insertKanbanCard(
      @PathVariable("kanban-column-id") Long kanbanColumnId,
      @RequestBody KanbanCardForm kanbanCardForm,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    return ResponseEntity.status(CREATED)
        .body(KANBAN_CARD_CREATED.withData(
            kanbanCardService.insertKanbanCard(kanbanColumnId, kanbanCardForm, customUserDetails.getId())));
  }
}
