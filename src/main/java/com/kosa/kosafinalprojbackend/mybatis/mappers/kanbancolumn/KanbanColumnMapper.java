package com.kosa.kosafinalprojbackend.mybatis.mappers.kanbancolumn;

import java.util.List;
import java.util.Map;

import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.ColumnDto;
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

  // 칸반 컬럼 조회
  List<ColumnDto> selectKanbanColumns(Long projectId);

  // 조회 (kanban_column 존재 여부)
  boolean existsKanbanColumns(Long kanbanColumnId);

  // 칸반 컬럼 삭제
  void deleteKanbanColumns(Long kanbanColumnId);
}
