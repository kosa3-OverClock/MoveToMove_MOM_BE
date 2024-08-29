package com.kosa.kosafinalprojbackend.mybatis.mappers.forderproject;

import com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto.ProjectDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FolderProjectMapper {

  // 조회 (특정 사용자의 모든 폴더 조회)
  List<ProjectDto> selectFolderAllProjects(@Param("folderId") Long folderId,
      @Param("memberId") Long memberId);

}
