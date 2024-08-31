package com.kosa.kosafinalprojbackend.domains.folder.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
  private int depth;                // 뎁스
  private int seq;                  // 순서

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdAt;  // 생성일시

}
