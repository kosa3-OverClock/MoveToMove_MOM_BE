package com.kosa.kosafinalprojbackend.mybatis.mappers.member;

import com.kosa.kosafinalprojbackend.domains.member.model.dto.MemberDto;
import com.kosa.kosafinalprojbackend.domains.member.model.form.SignUpForm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    
    // 아이디 기준 정보 조회
    MemberDto findByMemberId(Long memberId);

    // 이메일 기준 정보 조회
    MemberDto findByMemberEmail(String email);

    // 이메일 중복 확인
    boolean existsByMemberEmail(String email);
    
    // 닉네임 중복 확인
    boolean existsByMemberNickName(String nickName);
    
    // 회원가입
    void insertMember(SignUpForm signUpForm);

    // 조회 (memberId 기준)
    MemberDto findById(Long memberId);

    // 조회 (memberId 존재여부)
    boolean existsByMemberId(Long memberId);
}
