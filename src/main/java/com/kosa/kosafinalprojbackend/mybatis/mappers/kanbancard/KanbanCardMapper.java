package com.kosa.kosafinalprojbackend.mybatis.mappers.kanbancard;

import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.dto.CardDetailDto;
import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.dto.CardMemberDto;
import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.form.CardLocationForm;
import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.form.CardUpdateForm;
import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.form.CommentForm;
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

  // 칸반 카드 위치 수정
  void updateLocationKanbanCard(@Param("kanbanCardId") Long kanbanCardId,
                                @Param("cardLocationForm") CardLocationForm cardLocationForm);

  // 컬럼의 카드 순서 변경
  void updateKanbanCardSeq(@Param("cardLocationForm") CardLocationForm cardLocationForm,
                           @Param("symbol") boolean symbol);

  // 이전 컬럼 카드 순서 변경
  void updatePreKanbanCardSeq(@Param("kanbanColumn") Long kanbanColumn,
                              @Param("orderBy") String orderBy);

  // 칸반 카드 삭제
  void deleteKanbanCard(@Param("kanbanCardId") Long kanbanCardId);

  // 카드 코멘트 저장
  void insertComment(@Param("memberId") Long memberId,
                     @Param("kanbanCardId") Long kanbanCardId,
                     @Param("commentForm") CommentForm commentForm);
}
