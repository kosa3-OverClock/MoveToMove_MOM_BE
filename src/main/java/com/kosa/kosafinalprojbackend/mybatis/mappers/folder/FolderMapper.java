package com.kosa.kosafinalprojbackend.mybatis.mappers.folder;

import com.kosa.kosafinalprojbackend.domains.folder.model.dto.FolderDto;
import com.kosa.kosafinalprojbackend.domains.folder.model.dto.ProjectLeaderByFolderIdDto;
import com.kosa.kosafinalprojbackend.domains.folder.model.form.FolderForm;
import com.kosa.kosafinalprojbackend.domains.folder.model.form.FolderMoveForm;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FolderMapper {

    // 조회 (특정 사용자의 모든 폴더 조회)
    List<FolderDto> selectUserAllFolder(Long memberId);


    // 조회 (folderId 존재 여부 조회)
    boolean existsByFolderId(Long folderId);


    // 현재 최대 depth
    Integer selectMaxDepth(Long memberId);


    // 현재 최대 seq
    Integer selectMaxSeq(Long memberId);


    // 저장
    void insertFolder(@Param("folderForm") FolderDto folderDto,
        @Param("memberId") Long memberId);


    // 수정 (폴더명)
    void updateFolderName(@Param("folderId") Long folderId,
        @Param("folderForm") FolderForm folderForm, @Param("memberId") Long memberId);


    // 프로젝트 리더 여부 조회 (폴더 기준)
    List<ProjectLeaderByFolderIdDto> selectProjectLeaderYN(@Param("folderId") Long folderId,
        @Param("memberId") Long memberId);


    // 폴더 삭제
    void deleteFolder(Long folderId);


    // 조회(폴더 존재 여부 - 아이디 리스트)
    List<Long> selectExistingFolderIds(@Param("memberId") Long memberId,
        @Param("folderIds") List<Long> folderIds);


    // 수정 (폴더 위치 업데이트)
    void updateFolderMoves(@Param("memberId") Long memberId,
        @Param("folderMoveFormList") List<FolderMoveForm> folderMoveFormList);


    // 하위 폴더 조회
    List<FolderDto> selectFolderAllChildFolders(@Param("parentFolderId") Long parentFolderId,
        @Param("memberId") Long memberId);
}
