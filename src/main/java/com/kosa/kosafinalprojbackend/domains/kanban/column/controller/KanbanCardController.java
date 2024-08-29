package com.kosa.kosafinalprojbackend.domains.kanban.column.controller;

import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.KanbanColumnInCardDto;
import com.kosa.kosafinalprojbackend.domains.kanban.column.service.KanbanCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/kanban-cloumns")
public class KanbanCardController {

  private final KanbanCardService kanbanCardService;

  // 칸반 카드 조회 (칸반 컬럼 기준)
  @GetMapping("/{kanban-cloumn-id}/kanban-cards")
  public ResponseEntity<List<KanbanColumnInCardDto>> selectKanbanCardByKanbanColumn(
      @PathVariable("kanban-cloumn-id") Long kanbanColumnId) {

    return new ResponseEntity<>(
        kanbanCardService.selectKanbanCardByKanbanColumn(kanbanColumnId), HttpStatus.CREATED);
  }
}
