package com.kosa.kosafinalprojbackend.domains.folder.controller;

import com.kosa.kosafinalprojbackend.domains.folder.model.dto.FolderDto;
import com.kosa.kosafinalprojbackend.domains.folder.model.form.FolderForm;
import com.kosa.kosafinalprojbackend.domains.folder.service.FolderService;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectDto;
import com.kosa.kosafinalprojbackend.global.security.model.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  public ResponseEntity<List<FolderDto>> selectUserAllFolder(
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    return ResponseEntity.ok(folderService.selectUserAllFolder(customUserDetails));
  }


  // 특정 폴더안의 모든 프로젝트 조회
  @GetMapping("/{folder-id}/projects")
  public ResponseEntity<List<ProjectDto>> selectFolderAllProjects(
      @PathVariable("folder-id") Long folderId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    return ResponseEntity.ok(folderService.selectFolderAllProjects(folderId, customUserDetails));
  }


  // 폴더 생성
  @PostMapping
  public ResponseEntity<Long> insertFolder(@RequestBody FolderForm folderForm,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    return ResponseEntity.ok(folderService.insertFolder(folderForm, customUserDetails));
  }


  // 폴더 수정 (폴더명 + 다른 상위 폴더로 옮기기)
  @PutMapping("/{parent-folder-id}/{folder-id}")
  public ResponseEntity<Long> updateFolder(
      @PathVariable(value = "parent-folder-id", required = false) Long parentFolderId,
      @PathVariable("forder-id") Long folderId, @RequestBody FolderForm folderForm,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    folderService.updateFolder(parentFolderId, folderId, folderForm, customUserDetails);

    return ResponseEntity.ok().build();
  }


  // TODO: 진행해야함!!
  // 폴더 삭제
  @DeleteMapping("/{folder-id}")
  public ResponseEntity<Void> deleteFolder(@PathVariable(value = "folder-id") Long folderId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    folderService.deleteFolder(folderId, customUserDetails);
    return ResponseEntity.ok().build();
  }

}
