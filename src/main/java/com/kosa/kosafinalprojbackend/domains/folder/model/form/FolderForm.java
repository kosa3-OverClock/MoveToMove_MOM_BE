package com.kosa.kosafinalprojbackend.domains.folder.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FolderForm {

  private Long folderId;
  private String folderName;
  private int depth;
  private int seq;
}
