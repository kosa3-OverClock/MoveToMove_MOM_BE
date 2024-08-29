package com.kosa.kosafinalprojbackend.mybatis.mappers.folder;

import com.kosa.kosafinalprojbackend.domains.folder.model.dto.FolderDto;
import com.kosa.kosafinalprojbackend.domains.folder.model.dto.ProjectLeaderByFolderIdDto;
import com.kosa.kosafinalprojbackend.domains.folder.model.form.FolderForm;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FolderMapper {

  // 조회 (특정 사용자의 모든 폴더 조회)
  List<FolderDto> selectUserAllFolder(Long memberId);


  // 조회 (folderId 존재 여부 조회)
  boolean existsByFolderId(Long folderId);


  // 저장
  void insertFolder(@Param("folderForm") FolderForm folderForm,
      @Param("memberId") Long memberId);

  // 수정
  void updateFolder(@Param("parentFolderId") Long parentFolderId, @Param("folderId") Long folderId,
      FolderForm folderForm, @Param("memberId") Long memberId);


  // 프로젝트 리더 여부 조회 (폴더 기준)
  List<ProjectLeaderByFolderIdDto> selectProjectLeaderYN(@Param("folderId") Long folderId,
      @Param("memberId") Long memberId);
}
