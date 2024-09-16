package com.kosa.kosafinalprojbackend.mybatis.mappers.projectjoin;

import com.kosa.kosafinalprojbackend.domains.folder.model.dto.NotIncludedProjectDto;
import com.kosa.kosafinalprojbackend.domains.kanban.project.model.enums.ProjectLeaderYN;
import java.util.List;
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

    // 폴더 삭제 시 프로젝트_조인 삭제
    void deleteProjectJoinsByProjectIds(@Param("memberId") Long memberId,
        @Param("projectList") List<Long> projectList);


    // 조회 (프로젝트 아이디, 프로젝트 명, 팀장 여부)
    List<NotIncludedProjectDto> getProjectsByIds(List<Long> projectIds);

    // 팀장 권한을 한 번에 업데이트
    void updateTransferLeader(@Param("projectId") Long projectId, @Param("memberId") Long memberId,
        @Param("tranMemberId") Long tranMemberId);
}
