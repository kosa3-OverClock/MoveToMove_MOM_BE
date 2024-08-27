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
}
