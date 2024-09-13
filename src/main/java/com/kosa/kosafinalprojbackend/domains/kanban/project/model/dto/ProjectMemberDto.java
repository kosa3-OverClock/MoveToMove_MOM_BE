package com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto;

import com.kosa.kosafinalprojbackend.domains.kanban.project.model.enums.ProjectLeaderYN;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectMemberDto {

  private Long memberId;
  private String email;
  private String nickName;
  private String profileUrl;
  private ProjectLeaderYN projectLeaderYN;
}
