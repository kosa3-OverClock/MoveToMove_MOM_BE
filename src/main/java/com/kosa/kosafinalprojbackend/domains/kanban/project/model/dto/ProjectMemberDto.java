package com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectMemberDto {

  private Long memberId;
  private String email;
  private String nickName;
  private String profileUrl;
}
