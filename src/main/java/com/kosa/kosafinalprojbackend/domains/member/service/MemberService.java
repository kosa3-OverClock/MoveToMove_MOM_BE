package com.kosa.kosafinalprojbackend.domains.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosa.kosafinalprojbackend.domains.member.model.dto.MemberDto;
import com.kosa.kosafinalprojbackend.domains.member.model.form.LoginForm;
import com.kosa.kosafinalprojbackend.domains.member.model.form.SignUpForm;
import com.kosa.kosafinalprojbackend.global.amazon.service.S3Service;
import com.kosa.kosafinalprojbackend.global.error.exception.CustomBaseException;
import com.kosa.kosafinalprojbackend.global.redis.service.RedisService;
import com.kosa.kosafinalprojbackend.global.security.filter.JwtTokenProvider;
import com.kosa.kosafinalprojbackend.mybatis.mappers.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final S3Service s3Service;
  private final RedisService redisService;
  private final MemberMapper memberMapper;
  private final ObjectMapper objectMapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  // 로그인
  public String memberLogin(LoginForm signInForm) {

    // 아이디 확인
    MemberDto memberDto = memberMapper.findByMemberEmail(signInForm.getEmail());
    if (memberDto == null) {
      throw new CustomBaseException(UNABLE_LOGIN);
    }

    // 비밀번호 확인
    if (!passwordEncoder.matches(signInForm.getPassword(), memberDto.getPassword())) {
      throw new CustomBaseException(UNABLE_LOGIN);
    }

    // refresh token 생성
    String refreshToken =
        jwtTokenProvider.createRefreshToken(memberDto.getMemberId(), memberDto.getEmail());

    // Redis에 refresh token 저장
    redisService.saveRefreshToken(memberDto.getMemberId(), refreshToken);

    return jwtTokenProvider.createAccessToken(memberDto.getMemberId(), memberDto.getEmail());
  }

  // 회원가입
  public String memberSignUp(String signUpFormJson, MultipartFile multipartFile) {

    SignUpForm signUpForm;

    // json 문자열을 SignUpForm 객체로 변환
    try {
      signUpForm = objectMapper.readValue(signUpFormJson, SignUpForm.class);
    } catch (JsonProcessingException e) {
      throw new CustomBaseException(JSON_PARSE_ERROR);
    }

    // 이메일 중복 확인
    if (memberMapper.existsByMemberEmail(signUpForm.getEmail())) {
      throw new CustomBaseException(EXISTS_EMAIL);
    }

    // 닉네임 중복 확인
    if (memberMapper.existsByMemberNickName(signUpForm.getNickName())) {
      throw new CustomBaseException(EXISTS_NICKNAME);
    }

    // 이미지 S3에 저장
    if (multipartFile != null && !multipartFile.isEmpty()) {
      try {
        signUpForm.setProfileUrl(s3Service.uploadFile(multipartFile));
      } catch (Exception e) {
        throw new RuntimeException("이미지 업로드 실패", e);
      }
    }

    signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));

    memberMapper.insertMember(signUpForm);

    return signUpForm.getEmail();
  }

  // 회원 정보 수정
  @Transactional
  public String memberInfoChange(
      String signUpFormJson, MultipartFile multipartFile, Long memberId) {

    // 회원정보 조회
    MemberDto memberDto = memberMapper.findByMemberId(memberId);
    if (memberDto == null) {
      throw new CustomBaseException(NOT_FOUND_MEMBER);
    }

    SignUpForm signUpForm;
    // json 문자열을 SignUpForm 객체로 변환
    try {
      signUpForm = objectMapper.readValue(signUpFormJson, SignUpForm.class);
    } catch (JsonProcessingException e) {
      throw new CustomBaseException(JSON_PARSE_ERROR);
    }

    // S3 확인 후 이미지 변경 데이터 생성
    String imageUrl = memberDto.getProfileUrl();
    String existingFileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    boolean s3ImageFlag = s3Service.existsFile(existingFileName);

    // 수정 이미지가 있으면 (수정 or 저장) 없으면 삭제
    if (multipartFile != null && !multipartFile.isEmpty()) {
      // s3에 이미지가 있므면 수정 없다면 저장
      if (s3ImageFlag) {
        signUpForm.setProfileUrl(s3Service.updateFile(multipartFile, existingFileName));
      } else {
        signUpForm.setProfileUrl(s3Service.uploadFile(multipartFile));
      }
    } else {
      // s3에 이미지가 있으면 s3 Url 삭제
      if (s3ImageFlag) {
        signUpForm.setProfileUrl(s3Service.deleteFile(existingFileName));
      }
    }

    signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));

    memberMapper.updateMemberInfo(memberId, signUpForm);

    return memberDto.getEmail();
  }
}
