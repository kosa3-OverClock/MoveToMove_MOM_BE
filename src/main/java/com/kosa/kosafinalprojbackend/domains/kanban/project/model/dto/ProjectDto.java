package com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private Long projectId;                // 프로젝트 아이디
    private String projectName;            // 프로젝트 명
    private String projectDescription;     // 프로젝트 설명

    private LocalDateTime startAt;         // 시작일시
    private LocalDateTime endAt;           // 종료일시

    private LocalDateTime createdAt;       // 생성일시
    private LocalDateTime deletedAt;       // 삭제일시
}
