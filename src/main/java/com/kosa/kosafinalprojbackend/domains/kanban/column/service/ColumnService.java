package com.kosa.kosafinalprojbackend.domains.kanban.column.service;

import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.ColumnDto;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import com.kosa.kosafinalprojbackend.global.error.exception.CustomBaseException;
import com.kosa.kosafinalprojbackend.mybatis.mappers.kanbancolumn.KanbanColumnMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.project.ProjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ColumnService {

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

    // 칸반 컬럼 삭제
    public void deleteColumn(Long columnId) {
        // TODO:  칸반 컬럼에 다른 칸반 카드가 존재하는지 확인

        // TODO:  칸반 컬럼이 존재하는지 확인
        if(kanbanColumnMapper.existsKanbanColumns(columnId)) {
            log.info("존재하지 않는 컬럼입니다 ColumnId={}", columnId);
            throw new CustomBaseException(ResponseCode.NOT_FOUND_COLUMN);
        }
        kanbanColumnMapper.deleteKanbanColumns(columnId);
    }
}
