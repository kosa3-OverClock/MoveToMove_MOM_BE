package com.kosa.kosafinalprojbackend.mybatis.mappers.project;

import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectInCardDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectMemberDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.form.ProjectForm;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProjectMapper {

    // 저장
    Long insertProject(ProjectForm projectForm);

    // 조회 (projectId 존재 여부)
    boolean existsByProjectId(Long projectId);

    // 조회 (projectId 기준 projects 조회)
    Optional<ProjectDto> findByProjectId(Long projectId);

    // 수정
    void updateProject(@Param("projectId") Long projectId,
        @Param("projectForm") ProjectForm projectForm);

    // 프로젝트 참여자 조회
    List<ProjectMemberDto> selectProjectMember(Long projectId);

    // 칸반 카드 조회 (프로젝트 기준)
    List<ProjectInCardDto> selectKanbanCardByProject(Long projectId);
}
