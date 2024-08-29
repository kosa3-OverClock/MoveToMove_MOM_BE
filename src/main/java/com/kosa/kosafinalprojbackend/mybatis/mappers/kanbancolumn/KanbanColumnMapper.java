package com.kosa.kosafinalprojbackend.mybatis.mappers.kanbancolumn;

import java.util.List;
import java.util.Map;

import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.ColumnDto;
import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.KanbanColumnInCardDto;
import com.kosa.kosafinalprojbackend.domains.kanban.column.model.form.KanbanCardForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface KanbanColumnMapper {
  
  // 칸반 컬럼 존재 여부
  boolean existsByKanbanColumn(Long kanbanColumnId);

  // 칸반 카드 조회 (칸반 컬럼 기준)
  List<KanbanColumnInCardDto> selectKanbanCardByKanbanColumn(Long kanbanColumnId);

  // 칸반 카드 저장
  void insertKanbanCard(@Param("kanbanColumnId") Long kanbanColumnId,
                        @Param("kanbanCardForm") KanbanCardForm kanbanCardForm);

  // 칸반 카드 담당자 저장
  void insertKanbanCardWork(@Param("kanbanCardId") Long kanbanCardId,
                            @Param("memberList") List<Long> memberList);

  // 칸반 컬럼 저장
  void insertKanbanColumns(Map<String, Object> params);

  // 칸반 컬럼 조회
  List<ColumnDto> selectKanbanColumns(Long projectId);

  // 조회 (kanban_column 존재 여부)
  boolean existsKanbanColumns(Long kanbanColumnId);

  // 칸반 컬럼 삭제
  void deleteKanbanColumns(Long kanbanColumnId);
}
