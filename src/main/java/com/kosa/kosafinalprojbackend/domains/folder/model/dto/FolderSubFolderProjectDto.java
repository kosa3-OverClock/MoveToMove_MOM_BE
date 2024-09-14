package com.kosa.kosafinalprojbackend.domains.folder.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderSubFolderProjectDto implements FolderItem {

    private Long id;  // folderId 대신 id 사용
    private String title;  // folder_name 대신 label 사용
    private Long parentFolderId;
    private int depth;
    private int seq;
    private String projectIds;
    private String type;
    private List<Object> children;  // 하위 폴더와 프로젝트를 담는 nodes 리스트
}
