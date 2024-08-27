package com.kosa.kosafinalprojbackend.mybatis.mappers.member;

import com.kosa.kosafinalprojbackend.domains.member.oAuth.domain.Member;
import com.kosa.kosafinalprojbackend.domains.member.oAuth.domain.ProviderType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberOAuthMapper {

    Member findUserBySocialIdAndType(@Param("email") String email, @Param("providerType") ProviderType providerType);
    void insertMember(Member member);
}
