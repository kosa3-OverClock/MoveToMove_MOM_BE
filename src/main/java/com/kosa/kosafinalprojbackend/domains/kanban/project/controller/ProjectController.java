package com.kosa.kosafinalprojbackend.domains.kanban.project.controller;

import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.form.ProjectForm;
import com.kosa.kosafinalprojbackend.domains.kanban.project.service.ProjectService;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import com.kosa.kosafinalprojbackend.global.security.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;


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
    public ResponseEntity<ProjectDto> updateProject(@RequestBody ProjectDto project) {
        return ResponseEntity.ok().build();
    }


    // 삭제
    @DeleteMapping("/{project-id}")
    public ResponseEntity<ResponseCode> deleteProject(@PathVariable("project-id") String projectId) {

        projectService.deleteProject(projectId);

        return ResponseEntity.ok().build();
    }

}
