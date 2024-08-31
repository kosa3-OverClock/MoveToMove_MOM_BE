package com.kosa.kosafinalprojbackend.domains.folder.model.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FolderMoveForm {

  private Long folderId;
  private Long parentFolderId;
  private String folderName;
  private int depth;
  private int seq;
}
