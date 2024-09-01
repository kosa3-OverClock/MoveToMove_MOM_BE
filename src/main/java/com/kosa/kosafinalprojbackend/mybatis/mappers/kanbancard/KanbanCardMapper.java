package com.kosa.kosafinalprojbackend.mybatis.mappers.kanbancard;

import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.dto.CardDetailDto;
import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.dto.CardMemberDto;
import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.form.CardUpdateForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface KanbanCardMapper {

  // 칸반 카드 확인
  boolean existsByKanbanCardId(Long kanbanCardId);

  // 칸반 카드 정보 조회
  CardDetailDto selectKanbanCard(Long kanbanCardId);

  // 칸반 카드 담당자 조회
  List<CardMemberDto> selectKanbanCardMember(Long kanbanCardId);

  // 칸반 카드 담당자 삭제
  void deleteKanbanCardMember(@Param("kanbanCardId") Long kanbanCardId);

  // 칸반 카드 담당자 저장
  void insertKanbanCardMember(@Param("kanbanCardId") Long kanbanCardId,
                              @Param("memberList") List<Long> memberList);

  // 칸반 카드 수정
  void updateKanbanCard(@Param("kanbanCardId") Long kanbanCardId,
                        @Param("cardUpdateForm") CardUpdateForm kanCardUpdateForm);

  // 칸반 카드 삭제
  void deleteKanbanCard(@Param("kanbanCardId") Long kanbanCardId);
}
