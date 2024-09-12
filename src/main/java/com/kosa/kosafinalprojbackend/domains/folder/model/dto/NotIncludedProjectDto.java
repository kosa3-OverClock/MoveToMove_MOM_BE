package com.kosa.kosafinalprojbackend.domains.folder.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotIncludedProjectDto {

  private Long id;  // projectId 대신 id 사용
  private String title;
  private boolean projectLeaderYN;
}
