package com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectInCardDto {

  private Long projectId;
  private String projectName;
  private Long kanbanColumnId;
  private String kanbanColumnName;
  private Long columnSeq;
  private Long kanbanCardId;
  private String title;
  private String content;
  private Long cardSeq;
  private String priority;
  private String taskSize;
  private LocalDateTime startAt;
  private LocalDateTime endAt;
  private LocalDateTime createdAt;
}
