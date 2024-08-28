package com.kosa.kosafinalprojbackend.domains.member.controller;

import com.kosa.kosafinalprojbackend.domains.member.model.form.LoginForm;
import com.kosa.kosafinalprojbackend.domains.member.service.MemberService;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import com.kosa.kosafinalprojbackend.global.security.model.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

  private final MemberService memberService;

  // 로그인
  @PostMapping("/login")
  public ResponseEntity<ResponseCode> memberLogin(
      @RequestBody LoginForm signInForm, HttpServletResponse response) {

    return ResponseEntity.status(OK)
        .body(LOGIN_SUCCESS.withData(memberService.memberLogin(signInForm, response)));
  }

  // 회원가입
  @PostMapping(value = "/sign-up", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<ResponseCode> memberSignUp(
      @RequestPart(value = "form") String signUpFormJson,
      @RequestPart(value = "file", required = false) MultipartFile multipartFile) {

    return ResponseEntity.status(CREATED)
        .body(MEMBER_CREATED.withData(memberService.memberSignUp(signUpFormJson, multipartFile)));
  }

  // Refresh Token 확인
  @PostMapping("/checks/refresh-token")
  public ResponseEntity<ResponseCode> checkRefreshToken(
      @CookieValue(value = "refreshToken", required = false) String refreshToken) {

    return ResponseEntity.status(OK)
        .body(ISSUE_ACCESS_TOKEN.withData(memberService.checkRefreshToken(refreshToken)));
  }

  // 회원 정보 수정
  @PutMapping(value = "/info", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<ResponseCode> memberInfoChange(
      @RequestPart(value = "form") String signUpFormJson,
      @RequestPart(value = "file", required = false) MultipartFile multipartFile,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    return ResponseEntity.status(OK)
        .body(MEMBER_MODIFY_SUCCESS.withData(
            memberService.memberInfoChange(signUpFormJson, multipartFile, customUserDetails.getId())));
  }
}
