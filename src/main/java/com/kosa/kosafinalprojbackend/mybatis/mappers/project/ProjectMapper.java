package com.kosa.kosafinalprojbackend.mybatis.mappers.project;

import com.kosa.kosafinalprojbackend.domains.folder.model.dto.NotIncludedProjectDto;
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
    List<ProjectMemberDto> selectProjectMember(@Param("memberId") Long memberId,
        @Param("projectId") Long projectId);

    // 칸반 카드 조회 (프로젝트 기준)
    List<ProjectInCardDto> selectKanbanCardByProject(Long projectId);

    // 유저가 참여한 프로젝트 조회
    List<Long> selectProjectsIdByUserId(Long userId);

    // 삭제(프로젝트 업데이트 (deleted_at))
    void deleteProject(Long projectId);


    // 프로젝트 스토어 기준 조회
    NotIncludedProjectDto selectProjectByProjectId(Long projectId, Long memberId);
}
