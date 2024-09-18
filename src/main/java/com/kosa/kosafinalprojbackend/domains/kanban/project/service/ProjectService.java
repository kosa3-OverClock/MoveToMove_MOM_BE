package com.kosa.kosafinalprojbackend.domains.kanban.project.service;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.NOT_FOUND_ID;

import com.kosa.kosafinalprojbackend.domains.folder.model.dto.NotIncludedProjectDto;
import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.dto.CardMemberDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectCardDetailDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectInCardDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectMemberDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.enums.ProjectLeaderYN;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.form.ProjectForm;
import com.kosa.kosafinalprojbackend.domains.member.model.dto.MemberDto;
import com.kosa.kosafinalprojbackend.domains.member.service.EmailService;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import com.kosa.kosafinalprojbackend.global.error.exception.CustomBaseException;
import com.kosa.kosafinalprojbackend.global.security.model.CustomUserDetails;
import com.kosa.kosafinalprojbackend.mybatis.mappers.kanbancard.KanbanCardMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.kanbancolumn.KanbanColumnMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.member.MemberMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.project.ProjectMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.projectjoin.ProjectJoinMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
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
    private final KanbanColumnMapper kanbanColumnMapper;
    private final KanbanCardMapper kanbanCardMapper;
    private final EmailService emailService;


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

        // 기본 칸반 컬럼명 리스트
        List<String> columnNames = Arrays.asList("Task", "진행중", "완료");

        // 칸반 컬럼명과 순서를 한꺼번에 처리할 수 있도록 스트림으로 처리
        List<Map<String, Object>> columns = IntStream.range(0, columnNames.size())
            .mapToObj(i -> {
                Map<String, Object> columnData = new HashMap<>();
                columnData.put("column", columnNames.get(i));
                columnData.put("order", i + 1);
                return columnData;
            })
            .toList();

        // MyBatis 매퍼 호출하여 여러 칸반 컬럼을 한 번에 삽입
        kanbanColumnMapper.insertKanbanColumns(projectId, columns);

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
    public void deleteProject(Long projectId, ProjectLeaderYN projectLeaderYN,
        CustomUserDetails customUserDetails) {
        log.info("====>>>>>>>>>>> deleteProject: {}", projectId);

        if (!memberMapper.existsByMemberId(customUserDetails.getId())) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        if (!projectMapper.existsByProjectId(projectId)) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        // 팀장이라면
        if (projectLeaderYN == ProjectLeaderYN.Y) {

            List<Long> deleteProjectId = List.of(projectId);
            // project_joins 삭제
            projectJoinMapper.deleteProjectJoinsByProjectIds(customUserDetails.getId(),
                deleteProjectId);

            // project update deleted_at
            projectMapper.deleteProject(projectId);

        } else if (projectLeaderYN == ProjectLeaderYN.N) {
            // 팀장이 아니라면 project_joins만 삭제
            List<Long> deleteProjectId = List.of(projectId);
            // project_joins 삭제
            projectJoinMapper.deleteProjectJoinsByProjectIds(customUserDetails.getId(),
                deleteProjectId);
        }
    }


    // 프로젝트 참여자 조회
    @Transactional
    public List<ProjectMemberDto> selectProjectMember(Long memberId, Long projectId) {
        log.info("====>>>>>>>>>>>> selectProjectMember: {}", projectId);
        // 유저 아이디 확인
        if (!memberMapper.existsByMemberId(memberId)) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        // 프로젝트 존재 여부 확인
        if (!projectMapper.existsByProjectId(projectId)) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        return projectMapper.selectProjectMember(memberId, projectId);
    }

    // 칸반 카드 조회 (프로젝트 기준)
    @Transactional
    public List<ProjectCardDetailDto> selectKanbanCardByProject(Long projectId) {

        // 프로젝트 존재 여부 확인
        if (!projectMapper.existsByProjectId(projectId)) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        List<ProjectCardDetailDto> projectCardDetailDtoList = new ArrayList<>();

        // 프로젝트 기준 카드 조회
        List<ProjectInCardDto> projectInCardDtoList = projectMapper.selectKanbanCardByProject(
            projectId);

        // 프로젝트 카드 디테일
        for (ProjectInCardDto projectInCardDto : projectInCardDtoList) {

            // 칸반 카드 담당자 조회
            List<CardMemberDto> cardMemberList =
                kanbanCardMapper.selectKanbanCardMember(projectInCardDto.getKanbanCardId());

            projectCardDetailDtoList.add(
                ProjectCardDetailDto.builder()
                    .projectInCardDto(projectInCardDto)
                    .cardMemberList(cardMemberList)
                    .build());
        }

        return projectCardDetailDtoList;
    }

    // 유저가 참여한 프로젝트 ID 조회
    @Transactional
    public List<Long> selectProjectsIdByUserId(Long memberId) {
        if (!memberMapper.existsByMemberId(memberId)) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }
        return projectMapper.selectProjectsIdByUserId(memberId);
    }


    // 팀장 권한 이전
    @Transactional
    public void updateTransferLeader(Long projectId, Long tranMemberId, Long memberId) {
        log.info("====>>>>>>>>>>>> updateTransferLeader");

        if (!memberMapper.existsByMemberId(memberId)) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        if (!memberMapper.existsByMemberId(tranMemberId)) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        if (!projectMapper.existsByProjectId(projectId)) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        // 팀장 권한을 한 번에 업데이트
        projectJoinMapper.updateTransferLeader(projectId, memberId, tranMemberId);
    }


    // 프로젝트만 조회
    @Transactional
    public NotIncludedProjectDto selectProjectByProjectId(Long projectId, Long memberId) {

        if (!memberMapper.existsByMemberId(memberId)) {
            throw new CustomBaseException(NOT_FOUND_ID.withData("member"));
        }

        if(!projectMapper.existsByProjectId(projectId)) {
            throw new CustomBaseException(NOT_FOUND_ID.withData("project"));
        }

        return projectMapper.selectProjectByProjectId(projectId, memberId);
    }

    // 프로젝트 내보내기
    public void deleteReleaseMember(Long projectId, Long releaseMemberId, Long memberId) {

        // 유저 아이디 확인
        if (!memberMapper.existsByMemberId(memberId)) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        // 내보낼 아이디 확인
        if (!memberMapper.existsByMemberId(releaseMemberId)) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        // 프로젝트 확인
        if (!projectMapper.existsByProjectId(projectId)) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        projectJoinMapper.deleteReleaseMember(projectId, releaseMemberId);
    }


    // 프로젝트 참여자 저장 및 초대
    @Transactional
    public void insertProjectJoin(Long memberId, Long projectId, List<MemberDto> memberDtoList) {
        if(!memberMapper.existsByMemberId(memberId)) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        ProjectDto projectDto = projectMapper.findByProjectId(projectId)
            .orElseThrow(() -> new CustomBaseException(NOT_FOUND_ID));

        // insert 배치
        projectJoinMapper.insertProjectJoins(projectId, memberDtoList);
        
        // 초대 이메일 전송
        emailService.projectInvite(projectDto.getProjectName(), memberDtoList);
    }
}
