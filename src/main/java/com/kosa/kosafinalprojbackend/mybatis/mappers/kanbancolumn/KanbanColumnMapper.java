package com.kosa.kosafinalprojbackend.mybatis.mappers.kanbancolumn;

import java.util.List;
import java.util.Map;

import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.ColumnDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KanbanColumnMapper {

  // 칸반 컬럼 저장
  void insertKanbanColumns(Map<String, Object> params);

  // 칸반 컬럼 조회
  List<ColumnDto> selectKanbanColumns(Long projectId);

  // 조회 (kanban_column 존재 여부)
  boolean existsKanbanColumns(Long kanbanColumnId);

  // 칸반 컬럼 삭제
  void deleteKanbanColumns(Long kanbanColumnId);
}
