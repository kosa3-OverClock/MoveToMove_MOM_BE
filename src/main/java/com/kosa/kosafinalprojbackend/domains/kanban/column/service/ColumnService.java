package com.kosa.kosafinalprojbackend.domains.kanban.column.service;

import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.ColumnDto;
import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.KanbanColumnInCardDto;
import com.kosa.kosafinalprojbackend.domains.kanban.column.model.form.KanbanCardForm;
import com.kosa.kosafinalprojbackend.domains.kanban.column.model.form.KanbanColumnForm;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import com.kosa.kosafinalprojbackend.global.error.exception.CustomBaseException;
import com.kosa.kosafinalprojbackend.mybatis.mappers.kanbancolumn.KanbanColumnMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.member.MemberMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.project.ProjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.NOT_FOUND_ID;
import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.NOT_FOUND_KANBAN_COLUMN;

@Service
@Slf4j
@RequiredArgsConstructor
public class ColumnService {

    private final MemberMapper memberMapper;
    private final KanbanColumnMapper kanbanColumnMapper;
    private final ProjectMapper projectMapper;


    // 칸반 컬럼 조회
    public List<ColumnDto> selectColumn(Long projectId) {
        // 존재하지 않는 projectId일 경우 에러
        if(!projectMapper.existsByProjectId(projectId)) {
            log.info("존재하지 않는 ProjectId={}", projectId);
            throw new CustomBaseException(ResponseCode.NOT_FOUND_PROJECT);
        }

        // 컬럼 조회 (해당 프로젝트 기준 컬럼 조회)
        return kanbanColumnMapper.selectKanbanColumns(projectId);
    }

    // 칸반 컬럼 순서 수정, 새로운 컬럼 추가
    public void upsertColumn(List<KanbanColumnForm> kanbanColumnForm) {
        kanbanColumnMapper.upsertKanbanColumns(kanbanColumnForm);
    }

    // 칸반 컬럼 삭제
    @Transactional
    public void deleteColumn(Long columnId) {
        // TODO:  칸반 컬럼에 다른 칸반 카드가 존재하는지 확인

        // TODO:  칸반 컬럼이 존재하는지 확인
        if(kanbanColumnMapper.existsKanbanColumns(columnId)) {
            log.info("존재하지 않는 컬럼입니다 ColumnId={}", columnId);
            throw new CustomBaseException(ResponseCode.NOT_FOUND_COLUMN);
        }
        kanbanColumnMapper.deleteKanbanColumns(columnId);
    }


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
