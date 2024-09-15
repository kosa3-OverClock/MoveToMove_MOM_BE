package com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto;

import com.kosa.kosafinalprojbackend.domains.kanban.project.model.enums.ProjectLeaderYN;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectJoinDto {

    private Long memberId;
    private String email;
    private String nickName;
    private String profileUrl;
    private ProjectLeaderYN projectLeaderYN;
}
