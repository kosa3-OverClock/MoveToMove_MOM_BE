package com.kosa.kosafinalprojbackend.domains.kanban.card.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CardCommentDto {

  private Long commentId;
  private Long memberId;
  private String content;
  private LocalDateTime createdAt;
  private String email;
  private String nickName;
  private String profileUrl;
}
