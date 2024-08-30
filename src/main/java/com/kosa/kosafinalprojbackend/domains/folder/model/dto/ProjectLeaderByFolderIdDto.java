package com.kosa.kosafinalprojbackend.domains.folder.model.dto;

import com.kosa.kosafinalprojbackend.domains.kanban.project.model.enums.ProjectLeaderYN;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectLeaderByFolderIdDto {

  private Long projectId;
  private ProjectLeaderYN projectLeaderYN;
}
