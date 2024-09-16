package com.kosa.kosafinalprojbackend.mybatis.mappers.kanbancolumn;

import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.ColumnDto;
import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.KanbanColumnInCardDto;
import com.kosa.kosafinalprojbackend.domains.kanban.column.model.form.KanbanCardForm;
import com.kosa.kosafinalprojbackend.domains.kanban.column.model.form.KanbanColumnForm;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectInCardDto;
import java.util.List;
import java.util.Map;
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
                        @Param("kanbanCardForm") KanbanCardForm kanbanCardForm,
                        @Param("cardMaxSeq") int cardMaxSeq);

  // 칸반 카드 담당자 저장
  void insertKanbanCardWork(@Param("kanbanCardId") Long kanbanCardId,
                            @Param("memberList") List<Long> memberList);

  // 칸반 컬럼 저장
  void insertKanbanColumns(@Param("projectId") Long projectId, @Param("columns") List<Map<String, Object>> columns);

  // 칸반 컬럼 저장, 수정
  void upsertKanbanColumns(List<KanbanColumnForm> kanbanColumnForms);

  // 칸반 컬럼 조회
  List<ColumnDto> selectKanbanColumns(Long projectId);

  // 조회 (kanban_column 존재 여부)
  boolean existsKanbanColumns(Long kanbanColumnId);

  // 칸반 컬럼 삭제
  void deleteKanbanColumns(Long kanbanColumnId);

  // 칸반 컬럼 아이디 조회
  ColumnDto findByColumnId(Long columnId);

  // 컬럼 목록의 순서를 업데이트 (Batch Update)
  void updateColumnSeqBatch(@Param("columns") List<ColumnDto> columns);


  // 삭제된 컬럼 이후의 컬럼들 seq 업데이트
  void updateColumnSeqAfterDelete(@Param("projectId") Long projectId, @Param("deletedColumnSeq") int deletedColumnSeq);


  // 칸반 컬럼 기준 카드 MAX SEQ 조회
  int selectKanbanCardMaxSeqByKanbanColumnId(Long kanbanColumnId);


  // 나중에 dto 정리해야함
  // 칸반 카드 아이디 기준 컬럼 정보까지 조회 (코멘트까지 조회 x)
  ProjectInCardDto selectKanbanCardByKanbanCardId(Long kanbanCardId);
}
