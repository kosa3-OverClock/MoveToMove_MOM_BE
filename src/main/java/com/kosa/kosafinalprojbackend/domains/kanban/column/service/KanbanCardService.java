package com.kosa.kosafinalprojbackend.domains.kanban.column.service;

import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.KanbanColumnInCardDto;
import com.kosa.kosafinalprojbackend.domains.kanban.column.model.form.KanbanCardForm;
import com.kosa.kosafinalprojbackend.global.error.exception.CustomBaseException;
import com.kosa.kosafinalprojbackend.mybatis.mappers.kanbancolumn.KanbanColumnMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.NOT_FOUND_ID;
import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.NOT_FOUND_KANBAN_COLUMN;

@Service
@RequiredArgsConstructor
public class KanbanCardService {

  private final KanbanColumnMapper kanbanColumnMapper;
  private final MemberMapper memberMapper;

  // 칸반 카드 조회 (칸반 컬럼 기준)
  public List<KanbanColumnInCardDto> selectKanbanCardByKanbanColumn(Long kanbanColumnId) {

    // 칸반 카드 존재 여부 확인
    if (!kanbanColumnMapper.existsByKanbanColumn(kanbanColumnId)) {
      throw new CustomBaseException(NOT_FOUND_KANBAN_COLUMN);
    }

    return kanbanColumnMapper.selectKanbanCardByKanbanColumn(kanbanColumnId);
  }

  // 칸반 카드 생성
  @Transactional
  public Long insertKanbanCard(Long kanbanColumnId, KanbanCardForm kanbanCardForm, Long memberId) {

    // 유저 아이디 확인
    if (!memberMapper.existsByMemberId(memberId)) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    // 칸반 카드 존재 여부 확인
    if(!kanbanColumnMapper.existsByKanbanColumn(kanbanColumnId)) {
      throw new CustomBaseException(NOT_FOUND_KANBAN_COLUMN);
    }

    // 칸반 카드 저장
    kanbanColumnMapper.insertKanbanCard(kanbanColumnId, kanbanCardForm);

    // 칸반 카드 담당자 저장
    kanbanColumnMapper.insertKanbanCardWork(
        kanbanCardForm.getKanbanCardId(), kanbanCardForm.getMemberList());

    return kanbanCardForm.getKanbanCardId();
  }
}
