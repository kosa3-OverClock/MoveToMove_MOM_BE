package com.kosa.kosafinalprojbackend.mybatis.mappers.member;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    // 이메일 중복 확인
    boolean existsByEmail(String email);
}
