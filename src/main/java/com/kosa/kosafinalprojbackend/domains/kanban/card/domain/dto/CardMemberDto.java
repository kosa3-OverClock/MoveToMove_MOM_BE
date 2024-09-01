package com.kosa.kosafinalprojbackend.domains.kanban.card.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardMemberDto {

  private Long memberId;
  private String email;
  private String nickName;
  private String profileUrl;
}
