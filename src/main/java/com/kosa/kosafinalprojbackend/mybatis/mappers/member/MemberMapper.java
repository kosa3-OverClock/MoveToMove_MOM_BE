package com.kosa.kosafinalprojbackend.mybatis.mappers.member;

import com.kosa.kosafinalprojbackend.domains.member.model.form.SignUpForm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    // 이메일 중복 확인
    boolean existsByMemberEmail(String email);
    
    // 닉네임 중복 확인
    boolean existsByMemberNickName(String nickName);
    
    // 회원가입
    void insertMember(SignUpForm signUpForm);
}
