package com.kosa.kosafinalprojbackend.domains.kanban.column.service;

import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.ColumnDto;
import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.KanbanColumnInCardDto;
import com.kosa.kosafinalprojbackend.domains.kanban.column.model.form.KanbanCardForm;
import com.kosa.kosafinalprojbackend.domains.kanban.column.model.form.KanbanColumnForm;
import com.kosa.kosafinalprojbackend.domains.kanban.column.model.form.KanbanColumnMoveRequestForm;
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
    @Transactional
    public void upsertColumn(List<KanbanColumnForm> kanbanColumnForm) {
        kanbanColumnMapper.upsertKanbanColumns(kanbanColumnForm);
    }

    @Transactional
    public void updateKanbanColumn(Long kanbanColumnId, KanbanColumnMoveRequestForm kanbanColumnMoveRequestFoam) {
        // 변경하고자 하는 컬럼 객체
        ColumnDto targetColumn = kanbanColumnMapper.findByColumnId(kanbanColumnId);
        if(targetColumn == null) {
            // 조회한 컬럼이 존재하지 않은 오류
            log.info("존재하지 않는 컬럼입니다 ColumnId={}", kanbanColumnId);
            throw new CustomBaseException(ResponseCode.NOT_FOUND_COLUMN);
        }

        // 모든 컬럼 목록 조회 (컬럼시퀀스 순으로 조회)
        List<ColumnDto> allColumn = kanbanColumnMapper.selectKanbanColumns(kanbanColumnMoveRequestFoam.getProjectId());
        // 현재 컬럼의 위치와 새로운 위치 계산
        int columnPosition = targetColumn.getColumnSeq(); // 현재 컬럼 순서
        int newColumnPosition = kanbanColumnMoveRequestFoam.getNewPosition();
        if(columnPosition == newColumnPosition) {
            return; // 이동하는 위치와 현재 위치가 동일하기 때문에 이동할 필요 없음.
        }


        // 새로운 위치에 맞게 나머지 컬럼의 순서를 조정
        if(columnPosition < newColumnPosition) {  // 아래로 이동하는 경우
            for(ColumnDto columnDto : allColumn) {
                // 현재 컬럼 위치보다 크고, 새 위치보다 작거나 같은 컬럼들의 위치를 한 칸 앞으로 당김
                if(columnDto.getColumnSeq() > columnPosition && columnDto.getColumnSeq() <= newColumnPosition) {
                    columnDto.setColumnSeq(columnDto.getColumnSeq() -1); // 순서 앞으로 당김
                }
            }
        } else {    // 위로 이동하는 경우
            for(ColumnDto columnDto : allColumn) {
                if(columnDto.getColumnSeq() >= newColumnPosition && columnDto.getColumnSeq() < columnPosition) {
                    columnDto.setColumnSeq(columnDto.getColumnSeq() + 1);
                }
            }
        }

        // 이동할 컬럼의 위치 업데이트
        targetColumn.setColumnSeq(newColumnPosition);
        allColumn.add(targetColumn);
        // 컬럼들으 한 번에 저장 (Batch Update)
        kanbanColumnMapper.updateColumnSeqBatch(allColumn);
    }

    // 칸반 컬럼 삭제
    @Transactional
    public void deleteColumn(Long columnId) {
        // 칸반 컬럼이 존재하는지 확인
        if(!kanbanColumnMapper.existsKanbanColumns(columnId)) {
            log.info("존재하지 않는 컬럼입니다 ColumnId={}", columnId);
            throw new CustomBaseException(ResponseCode.NOT_FOUND_COLUMN);
        }
        ColumnDto targetColumn = kanbanColumnMapper.findByColumnId(columnId);

        // 삭제할 컬럼의 현재 순서를 찾는다.
        int deletedColumnSeq = targetColumn.getColumnSeq();
        // 칸반 컬럼 삭제 (soft delete)
        kanbanColumnMapper.deleteKanbanColumns(columnId);
        // 해당 순서보다 큰 컬름들의 순서를 하나씩 감소시킨다.
        kanbanColumnMapper.updateColumnSeqAfterDelete(targetColumn.getProjectId(), deletedColumnSeq);
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
