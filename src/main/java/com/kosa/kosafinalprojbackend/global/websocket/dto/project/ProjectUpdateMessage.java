package com.kosa.kosafinalprojbackend.global.websocket.dto.project;

import com.kosa.kosafinalprojbackend.domains.kanban.project.model.enums.ProjectLeaderYN;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ProjectUpdateMessage {
    private Long projectId;
    private String projectName;
    private String projectDescription;
    private LocalDateTime updateTime;
    private LocalDateTime endAt;
    private Map<Long, ProjectLeaderYN> MemberDtoMap = new HashMap<>();  // 초대한 멤버 리스트
    private String type;
}
