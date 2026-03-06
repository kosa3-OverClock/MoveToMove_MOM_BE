package com.kosa.kosafinalprojbackend.mybatis.mappers.projectinvite;

import com.kosa.kosafinalprojbackend.domains.member.model.dto.MemberDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProjectInviteMapper {

    void insertPendingInvites(@Param("projectId") Long projectId,
        @Param("memberList") List<MemberDto> memberDtoList);

    void updateInviteSuccess(@Param("projectId") Long projectId,
        @Param("memberId") Long memberId);

    void updateInviteFailed(@Param("projectId") Long projectId,
        @Param("memberId") Long memberId,
        @Param("failureReason") String failureReason);
}
