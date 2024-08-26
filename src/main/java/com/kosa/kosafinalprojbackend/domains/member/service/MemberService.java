package com.kosa.kosafinalprojbackend.domains.member.service;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.EXISTS_EMAIL;
import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.EXISTS_NICKNAME;
import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.JSON_PARSE_ERROR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosa.kosafinalprojbackend.domains.member.model.form.SignUpForm;
import com.kosa.kosafinalprojbackend.global.amazon.service.S3Service;
import com.kosa.kosafinalprojbackend.global.error.exception.CustomBaseException;
import com.kosa.kosafinalprojbackend.mybatis.mappers.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

  public final S3Service s3Service;
  public final MemberMapper memberMapper;
  private final ObjectMapper objectMapper;
  public final PasswordEncoder passwordEncoder;

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
