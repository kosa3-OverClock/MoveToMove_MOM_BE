package com.kosa.kosafinalprojbackend.domains.kanban.project.service;

import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectInCardDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectMemberDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.enums.ProjectLeaderYN;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.form.ProjectForm;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import com.kosa.kosafinalprojbackend.global.error.exception.CustomBaseException;
import com.kosa.kosafinalprojbackend.global.security.model.CustomUserDetails;
import com.kosa.kosafinalprojbackend.mybatis.mappers.kanbancolumn.KanbanColumnMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.member.MemberMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.project.ProjectMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.projectjoin.ProjectJoinMapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.NOT_FOUND_ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {

  private final ProjectMapper projectMapper;
  private final ProjectJoinMapper projectJoinMapper;
  private final MemberMapper memberMapper;
  private final KanbanColumnMapper kanbanColumnMapper;


  // 저장
  @Transactional
  public Long insertProject(ProjectForm projectForm, CustomUserDetails customUserDetails) {

    // 유저 아이디 확인
    if (!memberMapper.existsByMemberId(customUserDetails.getId())) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    // 프로젝트 저장
    projectMapper.insertProject(projectForm);
    Long projectId = projectForm.getProjectId();
    log.info("====>>>>>>>>>> projectId: {}", projectId);

    // 프로젝트 생성하는 유저 정보 넣기
    projectForm.getMemberDtoMap().put(customUserDetails.getId(), ProjectLeaderYN.Y);
    log.info("====>>>>>>>>>> getMemberDtoMap : {}", projectForm.getMemberDtoMap());

    // 프로젝트 참여 저장
    projectJoinMapper.insertProjectJoin(projectForm.getMemberDtoMap(), projectId);

    // 칸반 컬럼 기본 3개 저장
    Map<String, Object> params = new HashMap<>();

    // 기본 칸반 컬럼명 리스트
    List<String> columnNames = Arrays.asList("Task", "진행중", "완료");
    params.put("columns", columnNames);

    // MyBatis 매퍼 호출하여 여러 칸반 컬럼을 한 번에 삽입
    kanbanColumnMapper.insertKanbanColumns(projectId, params);

    //TODO: 프로젝트 팀 채팅방 넣어야함!!!

    return projectId;
  }

  // 수정
  @Transactional
  public void updateProject(Long projectId, ProjectForm projectForm,
      CustomUserDetails customUserDetails) {

    // 유저 아이디 확인
    if (!memberMapper.existsByMemberId(customUserDetails.getId())) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    // 프로젝트 팀장인지 확인
    if (!projectJoinMapper.existsByProjectIdAndMemberId(projectId, customUserDetails.getId())) {
      throw new CustomBaseException(ResponseCode.NO_PROJECT_LEADER);
    }

    // 프로젝트 아이디 확인
    if (!projectMapper.existsByProjectId(projectId)) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    // 프로젝트 정보 업데이트
    projectMapper.updateProject(projectId, projectForm);
  }

  // 삭제
  @Transactional
  public void deleteProject(String projectId) {

    // 폴더-프로젝트 삭제 - 물리적 삭제

    // 프로젝트 멤버 삭제 - 물리적 삭제

    // 프로젝트 팀 채팅 삭제
    // TODO: 프로젝트 팀 채팅 삭제

    // 프로젝트 삭제
  }


  // 프로젝트 참여자 조회
  public List<ProjectMemberDto> selectProjectMember(Long memberId, Long projectId) {

    // 유저 아이디 확인
    if (!memberMapper.existsByMemberId(memberId)) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    // 프로젝트 존재 여부 확인
    if (!projectMapper.existsByProjectId(projectId)) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    return projectMapper.selectProjectMember(projectId);
  }

  // 칸반 카드 조회 (프로젝트 기준)
  public List<ProjectInCardDto> selectKanbanCardByProject(Long projectId) {

    // 프로젝트 존재 여부 확인
    if (!projectMapper.existsByProjectId(projectId)) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    return projectMapper.selectKanbanCardByProject(projectId);
  }
}
