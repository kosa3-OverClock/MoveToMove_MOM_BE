package com.kosa.kosafinalprojbackend.mybatis.mappers.kanbancolumn;

import java.util.List;
import java.util.Map;

import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.KanbanColumnInCardDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KanbanColumnMapper {
  
  // 칸반 컬럼 존재 여부
  boolean existsByKanbanColumn(Long kanbanColumnId);

  // 칸반 카드 조회 (칸반 컬럼 기준)
  List<KanbanColumnInCardDto> selectKanbanCardByKanbanColumn(Long kanbanColumnId);

  // 칸반 컬럼 저장
  void insertKanbanColumns(Long projectId, Map<String, Object> params);
}
