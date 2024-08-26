package com.kosa.kosafinalprojbackend.domains.member.controller;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.MEMBER_CREATED;
import static org.springframework.http.HttpStatus.CREATED;

import com.kosa.kosafinalprojbackend.domains.member.service.MemberService;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

  private final MemberService memberService;

  // 회원가입
  @PostMapping(value = "/sign-up", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<ResponseCode> memberSignUp(
      @RequestPart(value = "form") String signUpFormJson,
      @RequestPart(value = "file", required = false) MultipartFile multipartFile) {

    return ResponseEntity
        .status(CREATED)
        .body(MEMBER_CREATED.withData(memberService.memberSignUp(signUpFormJson, multipartFile)));
  }
}
