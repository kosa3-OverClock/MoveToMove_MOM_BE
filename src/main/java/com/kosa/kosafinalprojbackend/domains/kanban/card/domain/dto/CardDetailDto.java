package com.kosa.kosafinalprojbackend.domains.kanban.card.domain.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDetailDto {

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
  private List<CardMemberDto> cardMemberList;
}
