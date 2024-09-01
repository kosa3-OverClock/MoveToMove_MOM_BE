package com.kosa.kosafinalprojbackend.domains.folder.service;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.NOT_FOUND_ID;

import com.kosa.kosafinalprojbackend.domains.folder.model.dto.FolderDto;
import com.kosa.kosafinalprojbackend.domains.folder.model.dto.ProjectLeaderByFolderIdDto;
import com.kosa.kosafinalprojbackend.domains.folder.model.form.FolderForm;
import com.kosa.kosafinalprojbackend.domains.folder.model.form.FolderMoveForm;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.enums.ProjectLeaderYN;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import com.kosa.kosafinalprojbackend.global.error.exception.CustomBaseException;
import com.kosa.kosafinalprojbackend.global.security.model.CustomUserDetails;
import com.kosa.kosafinalprojbackend.mybatis.mappers.folder.FolderMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.forderproject.FolderProjectMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.member.MemberMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.projectjoin.ProjectJoinMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderMapper folderMapper;
    private final MemberMapper memberMapper;
    private final FolderProjectMapper folderProjectMapper;
    private final ProjectJoinMapper projectJoinMapper;


    // 특정 사용자의 모든 폴더 조회
    public List<FolderDto> selectUserAllFolder(CustomUserDetails customUserDetails) {

        // 유저 존재 확인
        if (!memberMapper.existsByMemberId(customUserDetails.getId())) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        return folderMapper.selectUserAllFolder(customUserDetails.getId());
    }


    // 특정 폴더의 모든 프로젝트 조회
    public List<ProjectDto> selectFolderAllProjects(Long folderId,
        CustomUserDetails customUserDetails) {

        // 유저 존재 확인
        if (!memberMapper.existsByMemberId(customUserDetails.getId())) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        // 폴더 존재 확인
        if (!folderMapper.existsByFolderId(folderId)) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        return folderProjectMapper.selectFolderAllProjects(folderId, customUserDetails.getId());
    }


    // 폴더 생성
    @Transactional
    public Long insertFolder(FolderForm folderForm, CustomUserDetails customUserDetails) {

        // 유저 존재 확인
        if (!memberMapper.existsByMemberId(customUserDetails.getId())) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        // 현재 최대 depth와 seq 값을 조회
        Integer maxDepth = 1;
        Integer maxSeq = folderMapper.selectMaxSeq(customUserDetails.getId());

        // null 값 체크 후 0으로 초기화
        maxSeq = (maxSeq == null) ? 0 : maxSeq + 1;

        FolderDto folderDto = FolderDto.builder()
            .folderName(folderForm.getFolderName())
            .depth(maxDepth)
            .seq(maxSeq)
            .build();

        folderMapper.insertFolder(folderDto, customUserDetails.getId());
        return folderDto.getFolderId();
    }


    // 폴더 수정 (폴더명)
    @Transactional
    public void updateFolderName(Long folderId, FolderForm folderForm,
        CustomUserDetails customUserDetails) {

        // 유저 아이디 확인
        if (!memberMapper.existsByMemberId(customUserDetails.getId())) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        // 폴더 아이디 확인
        if (!folderMapper.existsByFolderId(folderId)) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        folderMapper.updateFolderName(folderId, folderForm, customUserDetails.getId());
    }


    // 폴더 수정 (폴더 옮기기)
    @Transactional
    public void updateFolderMoves(List<FolderMoveForm> folderMoveFormList, Long memberId) {

        // 유저 아이디 확인
        if (!memberMapper.existsByMemberId(memberId)) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        // 폴더 목록에서 최상위 폴더를 체크
        Long parentFolderId = folderMoveFormList.get(0).getParentFolderId();

        if (parentFolderId != null) {
            // 상위 폴더 아이디가 있는 경우: 모든 폴더가 유효한지 확인 후 업데이트 처리

            // 상위 폴더 존재 확인
            if (!folderMapper.existsByFolderId(parentFolderId)) {
                throw new CustomBaseException(NOT_FOUND_ID);
            }

            // 폴더 ID 리스트 추출
            List<Long> folderIds = folderMoveFormList.stream().map(FolderMoveForm::getFolderId).collect(Collectors.toList());

            // 존재하는 폴더 ID 리스트를 한 번에 가져오기
            List<Long> existingFolderIds = folderMapper.selectExistingFolderIds(memberId, folderIds);

            // 모든 폴더가 존재하는지 확인
            if (existingFolderIds.size() != folderIds.size()) {
                throw new CustomBaseException(NOT_FOUND_ID);
            }

        } else {
            // 최상위 폴더로 이동하는 경우

            // 폴더 존재 확인
            FolderMoveForm form = folderMoveFormList.get(0);

            if (!folderMapper.existsByFolderId(form.getFolderId())) {
                throw new CustomBaseException(NOT_FOUND_ID);
            }

            // 최상위 폴더의 경우 depth는 1, seq는 최대 seq + 1
            Integer maxDepth = 1;
            Integer maxSeq = folderMapper.selectMaxSeq(memberId);

            // null 값 체크 후 0으로 초기화
            maxSeq = (maxSeq == null) ? 0 : maxSeq + 1;

            // 빌더 패턴을 사용하여 새로운 FolderMoveForm 객체 생성
            FolderMoveForm updatedForm = FolderMoveForm.builder()
                .folderId(form.getFolderId())
                .parentFolderId(null)  // 최상위 폴더이므로 parentFolderId는 null
                .folderName(form.getFolderName())
                .depth(maxDepth)
                .seq(maxSeq)
                .build();

            // 기존 리스트의 첫 번째 요소를 새로 생성된 객체로 교체
            folderMoveFormList.set(0, updatedForm);

        }

        // MyBatis를 사용하여 리스트를 한 번에 업데이트
        folderMapper.updateFolderMoves(memberId, folderMoveFormList);

    }


    // 폴더 삭제
    @Transactional
    public void deleteFolder(Long folderId, CustomUserDetails customUserDetails) {

        // 유저 아이디 확인
        if (!memberMapper.existsByMemberId(customUserDetails.getId())) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        // 폴더 아이디 확인
        if (!folderMapper.existsByFolderId(folderId)) {
            throw new CustomBaseException(NOT_FOUND_ID);
        }

        // 폴더 안에 있는 프로젝트 삭제
        // 프로젝트 리더 여부 확인
        List<ProjectLeaderByFolderIdDto> leaderByFolderIdDtos =
            folderMapper.selectProjectLeaderYN(folderId, customUserDetails.getId());

        // 프로젝트 리더가 없는 경우 삭제 진행
        if (leaderByFolderIdDtos.stream()
            .noneMatch(dto -> ProjectLeaderYN.Y.equals(dto.getProjectLeaderYN()))) {

            // 삭제할 프로젝트 ID 목록 추출
            List<Long> projectIdList = leaderByFolderIdDtos.stream()
                .map(ProjectLeaderByFolderIdDto::getProjectId)
                .collect(Collectors.toList());

            if (!projectIdList.isEmpty()) {
                // IN 절을 사용하여 한 번에 프로젝트 조인 삭제
                projectJoinMapper.deleteProjectJoinsByProjectIds(customUserDetails.getId(),
                    projectIdList);
            }

            // 폴더 삭제 (= 업데이트)
            folderMapper.deleteFolder(folderId);

        } else {
            // 프로젝트 리더가 있는 경우 예외 처리
            throw new CustomBaseException(ResponseCode.YES_PROJECT_LEADER);
        }
    }


    // 하위 폴더 조회
    public List<FolderDto> selectFolderAllChildFolders(Long parentFolderId, Long memberId) {
        return folderMapper.selectFolderAllChildFolders(parentFolderId, memberId);
    }
}
