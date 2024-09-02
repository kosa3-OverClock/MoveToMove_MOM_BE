package com.kosa.kosafinalprojbackend.domains.kanban.card.service;

import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.dto.CardDetailDto;
import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.dto.CardMemberDto;
import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.form.CardLocationForm;
import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.form.CardUpdateForm;
import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.form.CardUpdateMemberForm;
import com.kosa.kosafinalprojbackend.domains.kanban.comment.domian.form.CommentForm;
import com.kosa.kosafinalprojbackend.global.error.exception.CustomBaseException;
import com.kosa.kosafinalprojbackend.mybatis.mappers.kanbancard.KanbanCardMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.member.MemberMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.NOT_FOUND_ID;
import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.NOT_FOUND_KANBAN_CARD;

@Service
@RequiredArgsConstructor
public class CardService {

  private final MemberMapper memberMapper;
  private final KanbanCardMapper kanbanCardMapper;

  // 칸반 카드 상세 조회
  public CardDetailDto selectKanbanCardDetail(Long memberId, Long kanbanCardId) {

    // 유저 아이디 확인
    if (!memberMapper.existsByMemberId(memberId)) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    // 칸반 카드 확인
    if (!kanbanCardMapper.existsByKanbanCardId(kanbanCardId)) {
      throw new CustomBaseException(NOT_FOUND_KANBAN_CARD);
    }

    // 칸반 카드 정보 조회
    CardDetailDto cardDetailDto = kanbanCardMapper.selectKanbanCard(kanbanCardId);

    // 칸반 카드 담당자 조회
    List<CardMemberDto> cardMemberList = kanbanCardMapper.selectKanbanCardMember(kanbanCardId);
    cardDetailDto.setCardMemberList(cardMemberList);

    return cardDetailDto;
  }


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

  // 칸반 카드 위치 수정
  @Transactional
  public void updateLocationKanbanCard(
      Long memberId, Long kanbanCardId, CardLocationForm cardLocationForm) {

    // 유저 아이디 확인
    if (!memberMapper.existsByMemberId(memberId)) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    // 칸반 카드 정보 조회
    CardDetailDto cardDetailDto = kanbanCardMapper.selectKanbanCard(kanbanCardId);

    // 칸반 카드 확인
    if (cardDetailDto == null) {
      throw new CustomBaseException(NOT_FOUND_KANBAN_CARD);
    }

    // 컬럼의 카드 순서 변경
    boolean check = cardDetailDto.getKanbanColumnId().equals(cardLocationForm.getKanbanColumnId());
    kanbanCardMapper.updateKanbanCardSeq(cardLocationForm, check);

    // 위치 수정
    kanbanCardMapper.updateLocationKanbanCard(kanbanCardId, cardLocationForm);
    TransactionAspectSupport.currentTransactionStatus().flush();

    // 이전 컬럼 카드 순서 변경
    check =
        cardDetailDto.getKanbanColumnId().equals(cardLocationForm.getKanbanColumnId()) &&
            cardDetailDto.getCardSeq() < cardLocationForm.getCardSeq();

    kanbanCardMapper.updatePreKanbanCardSeq(
        cardDetailDto.getKanbanColumnId(), check ? "ASC" : "DESC");
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

  // 카드 코멘트 저장
  @Transactional
  public void insertComment(Long memberId, Long kanbanCardId, CommentForm commentForm) {

    // 유저 아이디 확인
    if (!memberMapper.existsByMemberId(memberId)) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    // 칸반 카드 확인
    if (!kanbanCardMapper.existsByKanbanCardId(kanbanCardId)) {
      throw new CustomBaseException(NOT_FOUND_KANBAN_CARD);
    }

    kanbanCardMapper.insertComment(memberId, kanbanCardId, commentForm);
  }
}
