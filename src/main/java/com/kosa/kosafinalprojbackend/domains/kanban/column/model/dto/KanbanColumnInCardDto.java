package com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class KanbanColumnInCardDto {

  private Long kanbanColumnId;
  private String kanbanColumnName;
  private Long columnSeq;
  private Long kanbanCardId;
  private String title;
  private String content;
  private Long cardSeq;
  private Long priority;
  private Long taskSize;
  private LocalDateTime startAt;
  private LocalDateTime endAt;
  private LocalDateTime createdAt;
}
