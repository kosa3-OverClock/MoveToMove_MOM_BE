package com.kosa.kosafinalprojbackend.domains.folder.service;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.NOT_FOUND_ID;

import com.kosa.kosafinalprojbackend.domains.folder.model.dto.FolderDto;
import com.kosa.kosafinalprojbackend.domains.folder.model.dto.ProjectLeaderByFolderIdDto;
import com.kosa.kosafinalprojbackend.domains.folder.model.form.FolderForm;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectDto;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import com.kosa.kosafinalprojbackend.global.error.exception.CustomBaseException;
import com.kosa.kosafinalprojbackend.global.security.model.CustomUserDetails;
import com.kosa.kosafinalprojbackend.mybatis.mappers.folder.FolderMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.forderproject.FolderProjectMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.member.MemberMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.projectjoin.ProjectJoinMapper;
import java.util.List;
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

    folderMapper.insertFolder(folderForm, customUserDetails.getId());
    return folderForm.getFolderId();
  }


  // 폴더 수정
  @Transactional
  public void updateFolder(Long parentFolderId, Long folderId, FolderForm folderForm,
      CustomUserDetails customUserDetails) {

    // 상위 폴더 아이디 확인
    if (parentFolderId != null) {
      if (!folderMapper.existsByFolderId(parentFolderId)) {
        throw new CustomBaseException(NOT_FOUND_ID);
      }
    }

    // 폴더 아이디 확인
    if (!folderMapper.existsByFolderId(folderId)) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    // 유저 아이디 확인
    if(!memberMapper.existsByMemberId(customUserDetails.getId())) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    // 폴더 업데이트 (폴더명 + 다른 상위 폴더로 옮기기)
    folderMapper.updateFolder(parentFolderId, folderId, folderForm, customUserDetails.getId());
  }

  
  // 폴더 삭제
  @Transactional
  public void deleteFolder(Long folderId, CustomUserDetails customUserDetails) {

    // 유저 아이디 확인
    if(!memberMapper.existsByMemberId(customUserDetails.getId())) {
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

    //


      throw new CustomBaseException(ResponseCode.YES_PROJECT_LEADER);


  }
}
