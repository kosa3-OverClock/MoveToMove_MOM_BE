package com.kosa.kosafinalprojbackend.mybatis.mappers.kanbancard;

import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.form.CardUpdateForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface KanbanCardMapper {

  // 칸반 카드 확인
  boolean existsByKanbanCardId(Long kanbanCardId);

  // 칸반 카드 수정
  void updateKanbanCard(@Param("kanbanCardId") Long kanbanCardId,
                        @Param("cardUpdateForm") CardUpdateForm kanbanCardTitleForm);

  // 칸반 카드 삭제
  void deleteKanbanCard(@Param("kanbanCardId") Long kanbanCardId);
}
