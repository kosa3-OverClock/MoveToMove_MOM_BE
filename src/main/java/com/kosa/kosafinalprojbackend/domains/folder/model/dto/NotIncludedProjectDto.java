package com.kosa.kosafinalprojbackend.domains.folder.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotIncludedProjectDto implements FolderItem {

    private Long id;  // projectId 대신 id 사용
    private String title;
    private String projectDescription;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endAt;

    private boolean projectLeaderYN;

    private LocalDateTime createdAt;       // 생성일시
    private LocalDateTime deletedAt;       // 삭제일시
}
