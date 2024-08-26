package com.kosa.kosafinalprojbackend.mybatis.mappers.member;

import com.kosa.kosafinalprojbackend.domains.member.oAuth.domain.Member;
import com.kosa.kosafinalprojbackend.domains.member.oAuth.domain.ProviderType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberOAuthMapper {

    Member findUserBySocialIdAndType(String email, ProviderType providerType);
    void insertMember(Member member);
}
