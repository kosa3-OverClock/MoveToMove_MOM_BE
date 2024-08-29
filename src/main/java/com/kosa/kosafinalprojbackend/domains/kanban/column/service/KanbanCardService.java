package com.kosa.kosafinalprojbackend.domains.kanban.column.service;

import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.KanbanColumnInCardDto;
import com.kosa.kosafinalprojbackend.global.error.exception.CustomBaseException;
import com.kosa.kosafinalprojbackend.mybatis.mappers.kanbancolumn.KanbanColumnMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.NOT_FOUND_KANBAN_COLUMN;

@Service
@RequiredArgsConstructor
public class KanbanCardService {

  private final KanbanColumnMapper kanbanColumnMapper;

  // 칸반 카드 조회 (칸반 컬럼 기준)
  public List<KanbanColumnInCardDto> selectKanbanCardByKanbanColumn(Long kanbanColumnId) {

    // 칸반 카드 존재 여부 확인
    if (!kanbanColumnMapper.existsByKanbanColumn(kanbanColumnId)) {
      throw new CustomBaseException(NOT_FOUND_KANBAN_COLUMN);
    }

    return kanbanColumnMapper.selectKanbanCardByKanbanColumn(kanbanColumnId);
  }
}
