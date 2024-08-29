package com.kosa.kosafinalprojbackend.mybatis.mappers.projectjoin;

import com.kosa.kosafinalprojbackend.domains.kanban.project.model.enums.ProjectLeaderYN;
import com.kosa.kosafinalprojbackend.domains.member.model.dto.MemberDto;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProjectJoinMapper {

  // 저장
  void insertProjectJoin(@Param("memberDtoMap") Map<Long, ProjectLeaderYN> memberDtoMap,
      @Param("projectId") Long projectId);

  // 조회 (해당 프로젝트의 팀장 여부 조회)
  boolean existsByProjectIdAndMemberId(@Param("projectId") Long projectId,
      @Param("memberId") Long memberId);

}
