package com.kosa.kosafinalprojbackend.domains.kanban.card.service;

import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.dto.CardMemberDto;
import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.form.CardUpdateForm;
import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.form.CardUpdateMemberForm;
import com.kosa.kosafinalprojbackend.global.error.exception.CustomBaseException;
import com.kosa.kosafinalprojbackend.mybatis.mappers.kanbancard.KanbanCardMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.member.MemberMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.NOT_FOUND_ID;
import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.NOT_FOUND_KANBAN_CARD;

@Service
@RequiredArgsConstructor
public class CardService {

  private final MemberMapper memberMapper;
  private final KanbanCardMapper kanbanCardMapper;

  // 칸반 카드 담당자 수정
  @Transactional
  public void updateKanbanCardMember(
      Long memberId, Long kanbanCardId, CardUpdateMemberForm kanbanCardTitleForm) {

    // 유저 아이디 확인
    if (!memberMapper.existsByMemberId(memberId)) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    // 칸반 카드 확인
    if (!kanbanCardMapper.existsByKanbanCardId(kanbanCardId)) {
      throw new CustomBaseException(NOT_FOUND_KANBAN_CARD);
    }

    kanbanCardMapper.deleteKanbanCardMember(kanbanCardId);
    kanbanCardMapper.insertKanbanCardMember(kanbanCardId, kanbanCardTitleForm.getMemberIds());
  }

  // 칸반 카드 수정
  @Transactional
  public void updateKanbanCard(
      Long memberId, Long kanbanCardId, CardUpdateForm cardUpdateForm) {

    // 유저 아이디 확인
    if (!memberMapper.existsByMemberId(memberId)) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }
    
    // 칸반 카드 확인
    if (!kanbanCardMapper.existsByKanbanCardId(kanbanCardId)) {
      throw new CustomBaseException(NOT_FOUND_KANBAN_CARD);
    }

    kanbanCardMapper.updateKanbanCard(kanbanCardId, cardUpdateForm);
  }

  // 칸반 카드 삭제
  @Transactional
  public void deleteKanbanCard(Long memberId, Long kanbanCardId) {

    // 유저 아이디 확인
    if (!memberMapper.existsByMemberId(memberId)) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    // 칸반 카드 확인
    if (!kanbanCardMapper.existsByKanbanCardId(kanbanCardId)) {
      throw new CustomBaseException(NOT_FOUND_KANBAN_CARD);
    }

    kanbanCardMapper.deleteKanbanCard(kanbanCardId);
  }
}
