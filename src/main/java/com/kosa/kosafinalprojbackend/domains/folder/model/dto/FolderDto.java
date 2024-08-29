package com.kosa.kosafinalprojbackend.domains.folder.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FolderDto {

  private Long folderId;            // 폴더 아이디
  private Long memberId;            // 유저 아이디
  private Long parentFolderId;      // 상위 폴더 아이디
  private String folderName;        // 폴더명
  private LocalDateTime createdAt;  // 생성일시

}
