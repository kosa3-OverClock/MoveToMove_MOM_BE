package com.kosa.kosafinalprojbackend.domains.kanban.project.controller;

import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.ColumnDto;

import com.kosa.kosafinalprojbackend.domains.kanban.column.model.form.KanbanColumnForm;
import com.kosa.kosafinalprojbackend.domains.kanban.column.service.ColumnService;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectInCardDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectMemberDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.form.ProjectForm;
import com.kosa.kosafinalprojbackend.domains.kanban.project.service.ProjectService;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import com.kosa.kosafinalprojbackend.global.security.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ColumnService columnService;


    // 조회
    @GetMapping("/{project-id}")
    public ResponseEntity<ProjectDto> selectProjectById(@PathVariable("project-id") String projectId) {
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
    @DeleteMapping("/{project-id}")
    public ResponseEntity<ResponseCode> deleteProject(@PathVariable("project-id") String projectId) {

        projectService.deleteProject(projectId);
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
    public ResponseEntity<List<ProjectInCardDto>> selectKanbanCardByProject(
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
}
