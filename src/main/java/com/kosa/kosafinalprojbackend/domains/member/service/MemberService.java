package com.kosa.kosafinalprojbackend.domains.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosa.kosafinalprojbackend.domains.member.model.dto.AllTaskDto;
import com.kosa.kosafinalprojbackend.domains.member.model.dto.MemberDto;
import com.kosa.kosafinalprojbackend.domains.member.model.dto.ProjectByTaskDto;
import com.kosa.kosafinalprojbackend.domains.member.model.form.LoginForm;
import com.kosa.kosafinalprojbackend.domains.member.model.form.AuthenticationCodeForm;
import com.kosa.kosafinalprojbackend.domains.member.model.form.ResetPasswordForm;
import com.kosa.kosafinalprojbackend.domains.member.model.form.SignUpForm;
import com.kosa.kosafinalprojbackend.global.amazon.service.S3Service;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import com.kosa.kosafinalprojbackend.global.error.exception.CustomBaseException;
import com.kosa.kosafinalprojbackend.global.redis.service.RedisService;
import com.kosa.kosafinalprojbackend.global.security.filter.JwtTokenProvider;
import com.kosa.kosafinalprojbackend.mybatis.mappers.member.MemberMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
  private final EmailService emailService;
  private final MemberMapper memberMapper;
  private final ObjectMapper objectMapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  @Value("${refresh.expire_time}")
  private int REFRESH_EXPIRE_TIME;

  // 회원 조회
  public MemberDto selectMemberInfo(Long memberId) {

    // 유저 아이디 확인
    if (!memberMapper.existsByMemberId(memberId)) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    return memberMapper.findByMemberId(memberId);
  }

  // 로그인
  public String memberLogin(LoginForm signInForm, HttpServletResponse response) {

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

    // refresh token 쿠키에 저장
    Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
    refreshTokenCookie.setHttpOnly(true);   // 스크립트에서 접근 불가
    refreshTokenCookie.setSecure(false);    // HTTPS를 사용하는 경우에 사용
    refreshTokenCookie.setPath("/");        // 쿠키 경로 설정
    refreshTokenCookie.setMaxAge(REFRESH_EXPIRE_TIME / 1000); // 시간 설정
    response.addCookie(refreshTokenCookie);

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
    if (memberMapper.existsByMemberNickName(signUpForm.getNickName(), null)) {
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

  // Refresh Token 확인
  public String checkRefreshToken(String token) {

    // refresh Token 으로 사용자 찾기
    Long memberId = jwtTokenProvider.getMemberByToken(token, false);

    // redis refreshToken
    String redisRefreshToken = redisService.selectRefreshToken(memberId);
    Long redisMemberId = jwtTokenProvider.getMemberByToken(redisRefreshToken, false);

    // 토큰 비교
    if (token.equals(redisRefreshToken) && memberId.equals(redisMemberId)) {
      MemberDto memberDto = memberMapper.findByMemberId(memberId);
      return jwtTokenProvider.createAccessToken(memberId, memberDto.getEmail());
    } else {
      throw new CustomBaseException(NOT_FIND_TOKEN);
    }
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

    // 닉네임 중복 확인
    if (memberMapper.existsByMemberNickName(signUpForm.getNickName(), memberId)) {
      throw new CustomBaseException(EXISTS_NICKNAME.withData("nickName"));
    }

    // S3 확인 후 이미지 변경 데이터 생성
    String imageUrl = memberDto.getProfileUrl();
    String existingFileName = "";
    if (imageUrl != null) {
      existingFileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }
    boolean s3ImageFlag = s3Service.existsFile(existingFileName);

    // 수정 이미지가 있으면 (수정 or 저장) 없으면 삭제
    if (multipartFile != null && !multipartFile.isEmpty()) {
      // s3에 이미지가 있므면 수정 없다면 저장
      if (s3ImageFlag) {
        signUpForm.setProfileUrl(s3Service.updateFile(multipartFile, existingFileName));
      } else {
        signUpForm.setProfileUrl(s3Service.uploadFile(multipartFile));
      }
    }

    if (!signUpForm.getPassword().isEmpty()) {
      signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
    }

    memberMapper.updateMemberInfo(memberId, signUpForm);

    return memberDto.getEmail();
  }

  // 회원 탈퇴
  @Transactional
  public void memberWithdraw(HttpServletResponse response, Long memberId) {

    // 유저 아이디 확인
    if (!memberMapper.existsByMemberId(memberId)) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    // 쿠키 refresh 토큰 삭제
    Cookie refreshTokenCookie = new Cookie("refreshToken", null);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setSecure(false);
    refreshTokenCookie.setPath("/");
    refreshTokenCookie.setMaxAge(0);
    response.addCookie(refreshTokenCookie);

    // redis에 저장된 refresh 토큰 삭제
    redisService.deleteRefreshToken(memberId);

    // DB에 저장된 회원 delete_at 값 생성 (access 토큰은 프론트 처리)
    memberMapper.memberWithdraw(memberId);
  }

  // 인증번호 발송
  @Transactional
  public Map<String, Object> sendAuthenticationCode(AuthenticationCodeForm authenticationCodeForm)
      throws MessagingException {
    String email = authenticationCodeForm.getEmail();

    // 메일 확인
    if(!memberMapper.existsByMemberEmail(email)) {
      throw new CustomBaseException(NOT_FOUND_MEMBER.withData("checkEmail"));
    }
    
    // 메일 보내기 (+ 인증 코드 받아오기)
    String code = emailService.sendVerificationCode(email);

    // Redis에 인증번호와 만료시간 저장
    String expiresIn = redisService.saveVerificationCode(email, code);

    // map 으로 return
    Map<String, Object> result = new HashMap<>();
    result.put("email", email);
    result.put("code", code);
    result.put("expiresIn", expiresIn);

    return result;
  }

  // 인증 코드 확인
  @Transactional
  public boolean verifyCode(String email, String code) {
    String storedCode = redisService.getVerificationCode(email);
    return storedCode != null && storedCode.equals(code);
  }

  // 비밀번호 변경
  @Transactional
  public boolean memberPasswordReset(ResetPasswordForm resetPasswordForm) {

    if(verifyCode(resetPasswordForm.getEmail(), resetPasswordForm.getCode())) {
      MemberDto memberDto = memberMapper.findByMemberEmail(resetPasswordForm.getEmail());

      memberMapper.updateMemberPassword(memberDto.getMemberId(),
          passwordEncoder.encode(resetPasswordForm.getNewPassword()));

      // 인증코드 제거
      redisService.deleteVerificationCode(resetPasswordForm.getEmail());

      return true;
    }

    return false;
  }


  // 마이페이지 - task
  @Transactional
  public AllTaskDto selectAllTask(Long memberId) {

    // 유저 아이디 확인
    if (!memberMapper.existsByMemberId(memberId)) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    return memberMapper.selectAllTask(memberId);
  }

  // 마이페이지 - 프로젝트별 task
  @Transactional
  public List<ProjectByTaskDto> selectProjectByTask(Long memberId) {

    // 유저 아이디 확인
    if (!memberMapper.existsByMemberId(memberId)) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    return memberMapper.selectProjectByTask(memberId);
  }
}
