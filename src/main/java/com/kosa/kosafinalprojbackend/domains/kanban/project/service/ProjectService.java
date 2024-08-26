package com.kosa.kosafinalprojbackend.domains.kanban.project.service;

import com.kosa.kosafinalprojbackend.domains.kanban.project.model.enums.ProjectLeaderYN;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.form.ProjectForm;
import com.kosa.kosafinalprojbackend.domains.member.model.dto.MemberDto;
import com.kosa.kosafinalprojbackend.global.security.model.CustomUserDetails;
import com.kosa.kosafinalprojbackend.mybatis.mappers.member.MemberMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.project.ProjectMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.projectjoin.ProjectJoinMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectMapper projectMapper;
    private final ProjectJoinMapper projectJoinMapper;
    private final MemberMapper memberMapper;


    // 저장
    @Transactional
    public Long insertProject(ProjectForm projectForm, CustomUserDetails customUserDetails) {
        
        // 프로젝트 저장
        Long projectId = projectMapper.insertProject(projectForm);

        // 프로젝트 생성하는 유저 정보 넣기
//        projectForm.getMemberDtoMap().put(customUserDetails.getId(), ProjectLeaderYN.Y);
        projectForm.getMemberDtoMap().put(1L, ProjectLeaderYN.Y);

        log.info("====>>>>>>>>>> getMemberDtoMap : {}", projectForm.getMemberDtoMap());

        // 프로젝트 참여 저장
        projectJoinMapper.insertProjectJoin(projectForm.getMemberDtoMap(), projectId);

        //TODO: 프로젝트 팀 채팅방 넣어야함!!!

        return projectId;
    }

    // 삭제
    @Transactional
    public void deleteProject(String projectId) {

        // 폴더-프로젝트 삭제 - 물리적 삭제

        // 코멘트 삭제

        // 칸반 카드 삭제

        // 칸반 컬럼 삭제

        // 프로젝트 멤버 삭제 - 물리적 삭제

        // 프로젝트 팀 채팅 삭제

        // 프로젝트 삭제
    }
}
