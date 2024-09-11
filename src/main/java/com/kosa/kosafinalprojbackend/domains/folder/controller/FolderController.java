package com.kosa.kosafinalprojbackend.domains.folder.controller;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.FOLDER_CREATED;
import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.FOLDER_DELETED;
import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.FOLDER_MODIFY_MOVE;
import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.FOLDER_MODIFY_NAME;
import static org.springframework.http.HttpStatus.OK;

import com.kosa.kosafinalprojbackend.domains.folder.model.dto.FolderDto;
import com.kosa.kosafinalprojbackend.domains.folder.model.dto.FolderSubFolderProjectDto;
import com.kosa.kosafinalprojbackend.domains.folder.model.form.FolderForm;
import com.kosa.kosafinalprojbackend.domains.folder.model.form.FolderMoveForm;
import com.kosa.kosafinalprojbackend.domains.folder.service.FolderService;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectDto;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import com.kosa.kosafinalprojbackend.global.security.model.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;


    // 특정 사용자의 모든 폴더 조회
    @GetMapping
    public ResponseEntity<List<FolderSubFolderProjectDto>> selectUserAllFolder(
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok(folderService.selectUserAllFolder(customUserDetails));
    }


    // 특정 폴더안의 모든 프로젝트 조회
    @GetMapping("/{folder-id}/projects")
    public ResponseEntity<List<ProjectDto>> selectFolderAllProjects(
        @PathVariable("folder-id") Long folderId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok(
            folderService.selectFolderAllProjects(folderId, customUserDetails));
    }


    // 폴더 생성
    @PostMapping
    public ResponseEntity<ResponseCode> insertFolder(@RequestBody FolderForm folderForm,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(
                FOLDER_CREATED.withData(folderService.insertFolder(folderForm, customUserDetails)));
    }


    // 폴더 수정 (폴더명)
    @PatchMapping("/{folder-id}")
    public ResponseEntity<ResponseCode> updateFolderName(@PathVariable("folder-id") Long folderId,
        @RequestBody FolderForm folderForm,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        folderService.updateFolderName(folderId, folderForm, customUserDetails);
        return ResponseEntity.status(OK).body(FOLDER_MODIFY_NAME);
    }


    // 폴더 위치 옮기기
    @PatchMapping("/folder-moves")
    public ResponseEntity<ResponseCode> updateFolderMoves(
        @RequestBody List<FolderMoveForm> folderMoveForm,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        folderService.updateFolderMoves(folderMoveForm, customUserDetails.getId());
        return ResponseEntity.status(OK).body(FOLDER_MODIFY_MOVE);
    }


    // 폴더 삭제
    @DeleteMapping("/{folder-id}")
    public ResponseEntity<ResponseCode> deleteFolder(@PathVariable("folder-id") Long folderId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        folderService.deleteFolder(folderId, customUserDetails);
        return ResponseEntity.status(OK).body(FOLDER_DELETED);
    }


    // 하위 폴더 조회
    @GetMapping("/{parent-folder-id}/child-folders")
    public ResponseEntity<List<FolderDto>> selectFolderAllChildFolders(
        @PathVariable("parent-folder-id") Long parentFolderId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity
            .status(OK)
            .body(folderService.selectFolderAllChildFolders(parentFolderId,
                customUserDetails.getId()));
    }

}
