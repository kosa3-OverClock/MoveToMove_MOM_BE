package com.kosa.kosafinalprojbackend.domains.folder.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalFolderProjectDto {

    private List<FolderItem> totalFolderProjects;
}
