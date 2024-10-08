package com.kosa.kosafinalprojbackend.domains.folder.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.enums.ProjectLeaderYN;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectAndJoinDto {

  private Long projectId;                // 프로젝트 아이디
  private String projectName;            // 프로젝트 명
  private String projectDescription;     // 프로젝트 설명

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime startAt;         // 시작일시
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime endAt;           // 종료일시

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdAt;       // 생성일시
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime deletedAt;       // 삭제일시
  
  private ProjectLeaderYN projectLeaderYN;// 프로젝트 팀장 여부
}
