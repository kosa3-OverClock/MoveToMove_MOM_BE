package com.kosa.kosafinalprojbackend.domains.kanban.project.controller;

import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.ColumnDto;
import com.kosa.kosafinalprojbackend.domains.kanban.column.service.ColumnService;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectCardDetailDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectMemberDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.enums.ProjectLeaderYN;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.form.ProjectForm;
import com.kosa.kosafinalprojbackend.domains.kanban.project.service.ProjectService;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import com.kosa.kosafinalprojbackend.global.security.model.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ColumnService columnService;


    // 조회
    @GetMapping("/{project-id}")
    public ResponseEntity<ProjectDto> selectProjectById(
        @PathVariable("project-id") String projectId) {
        return ResponseEntity.ok().build();
    }


    // 저장
    @PostMapping
    public ResponseEntity<ResponseCode> insertProject(@RequestBody ProjectForm projectForm,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok(ResponseCode.PROJECT_CREATE
            .withData(projectService.insertProject(projectForm, customUserDetails)));
    }


    // 수정
    @PutMapping("/{project-id}")
    public ResponseEntity<ResponseCode> updateProject(@PathVariable("project-id") Long projectId,
        @RequestBody ProjectForm projectForm,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        projectService.updateProject(projectId, projectForm, customUserDetails);
        return ResponseEntity.ok(ResponseCode.PROJECT_MODIFY);
    }


    // 삭제
    @DeleteMapping("/{project-id}/{project-leader}")
    public ResponseEntity<ResponseCode> deleteProject(@PathVariable("project-id") Long projectId,
        @PathVariable("project-leader") ProjectLeaderYN projectLeaderYN,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        projectService.deleteProject(projectId, projectLeaderYN, customUserDetails);
        return ResponseEntity.ok().build();
    }

    // 프로젝트 참여자 조회
    @GetMapping("/{project-id}/members")
    public ResponseEntity<List<ProjectMemberDto>> selectProjectMember(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @PathVariable("project-id") Long projectId) {

        return ResponseEntity.ok(
            projectService.selectProjectMember(customUserDetails.getId(), projectId));
    }

    // 칸반 카드 조회 (프로젝트 기준)
    @GetMapping("/{project-id}/kanban-cards")
    public ResponseEntity<List<ProjectCardDetailDto>> selectKanbanCardByProject(
        @PathVariable("project-id") Long projectId) {

        return new ResponseEntity<>(
            projectService.selectKanbanCardByProject(projectId), HttpStatus.CREATED);
    }

    // 칸반 컬럼 조회
    @GetMapping("/{project-id}/kanban-columns")
    public ResponseEntity<List<ColumnDto>> getAllColumnsByProjectId(
        @PathVariable("project-id") Long projectId) {

        return new ResponseEntity<>(columnService.selectColumn(projectId), HttpStatus.OK);
    }

    // WebSocket 연결에서 사용 - 사용자가 참여중인 프로젝트 ID 조회
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Long>> selectMemberById(
        @PathVariable("memberId") Long memberId) {

        return new ResponseEntity<>(projectService.selectProjectsIdByUserId(memberId),
            HttpStatus.OK);
    }

    // 팀장 권한 이전
    @PatchMapping("/{project-id}/project-join/{tran-member-id}/transfer-leader")
    public ResponseEntity<Void> updateTransferLeader(@PathVariable("project-id") Long projectId,
        @PathVariable("tran-member-id") Long tranMemberId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        projectService.updateTransferLeader(projectId, tranMemberId, customUserDetails.getId());
        return ResponseEntity.ok().build();
    }
}
