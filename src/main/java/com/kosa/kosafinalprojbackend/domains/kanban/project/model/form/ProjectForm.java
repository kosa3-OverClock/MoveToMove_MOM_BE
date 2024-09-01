package com.kosa.kosafinalprojbackend.domains.kanban.project.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.enums.ProjectLeaderYN;
import com.kosa.kosafinalprojbackend.domains.member.model.dto.MemberDto;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ProjectForm {

    private Long projectId;             // 프로젝트 아이디
    private String projectName;         // 프로젝트 명
    private String projectDescription;  // 프로젝트 설명

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startAt;      // 시작일시

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endAt;        // 종료일시

    private Map<Long, ProjectLeaderYN> MemberDtoMap = new HashMap<>();  // 초대한 멤버 리스트

}
