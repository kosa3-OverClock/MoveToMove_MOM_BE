package com.kosa.kosafinalprojbackend.domains.member.controller;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.ISSUE_ACCESS_TOKEN;
import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.LOGIN_SUCCESS;
import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.MEMBER_CREATED;
import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.MEMBER_MODIFY_SUCCESS;
import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.SEND_AUTHENTICATION_CODE;
import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.VERIFY_FAILED;
import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.VERIFY_SUCCESS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.kosa.kosafinalprojbackend.domains.member.model.dto.AllTaskDto;
import com.kosa.kosafinalprojbackend.domains.member.model.dto.MemberDto;
import com.kosa.kosafinalprojbackend.domains.member.model.dto.ProjectByTaskDto;
import com.kosa.kosafinalprojbackend.domains.member.model.dto.SearchEmail;
import com.kosa.kosafinalprojbackend.domains.member.model.form.AuthenticationCodeForm;
import com.kosa.kosafinalprojbackend.domains.member.model.form.LoginForm;
import com.kosa.kosafinalprojbackend.domains.member.model.form.ResetPasswordForm;
import com.kosa.kosafinalprojbackend.domains.member.service.EmailService;
import com.kosa.kosafinalprojbackend.domains.member.service.MemberService;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import com.kosa.kosafinalprojbackend.global.security.model.CustomUserDetails;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;


    // 회원 조회
    @GetMapping()
    public ResponseEntity<MemberDto> selectMemberInfo(
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok(memberService.selectMemberInfo(customUserDetails.getId()));
    }

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
            .body(
                MEMBER_CREATED.withData(memberService.memberSignUp(signUpFormJson, multipartFile)));
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
                memberService.memberInfoChange(signUpFormJson, multipartFile,
                    customUserDetails.getId())));
    }

    // 회원 탈퇴
    @DeleteMapping("/withdraw")
    public ResponseEntity<ResponseCode> memberWithdraw(HttpServletResponse response,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        memberService.memberWithdraw(response, customUserDetails.getId());

        return ResponseEntity.ok(ResponseCode.MEMBER_DELETE);
    }

    // 비밀번호 찾기 (=변경)
    // 인증 코드 받기 (= 인증 코드 메일 보내기)
    @PostMapping("/password/send-authentication-code")
    public ResponseEntity<ResponseCode> sendAuthenticationCode(
        @RequestBody AuthenticationCodeForm authenticationCodeForm) throws MessagingException {

        Map<String, Object> emailAndNumber = memberService.sendAuthenticationCode(
            authenticationCodeForm);

        // 메일 보내기
        emailService.sendVerificationCode((String) emailAndNumber.get("email"),
            (String) emailAndNumber.get("code"));

        return ResponseEntity.ok(SEND_AUTHENTICATION_CODE.withData(emailAndNumber));
    }

    // 인증 코드 확인
    @PostMapping("/password/authentication-code/verify-code")
    public ResponseEntity<ResponseCode> memberVerifyCode(
        @RequestBody AuthenticationCodeForm authenticationCodeForm) {

        if (memberService.verifyCode(authenticationCodeForm.getEmail(),
            authenticationCodeForm.getCode())) {

            return ResponseEntity.ok(VERIFY_SUCCESS);

        } else {
            return ResponseEntity.status(BAD_REQUEST).body(VERIFY_FAILED);
        }
    }

    // 비밀번호 변경
    @PostMapping("/password/reset")
    public ResponseEntity<ResponseCode> memberPasswordReset(
        @RequestBody ResetPasswordForm resetPasswordForm) {

        if(memberService.memberPasswordReset(resetPasswordForm)) {
            return ResponseEntity.ok(ResponseCode.UPDATE_PASSWORD_SUCCESS);

        } else {
            return ResponseEntity.status(BAD_REQUEST).body(VERIFY_FAILED);
        }
    }


    // 마이페이지 - task
    @GetMapping("/all-tasks")
    public ResponseEntity<AllTaskDto> selectAllTask(
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok(memberService.selectAllTask(customUserDetails.getId()));
    }

    // 마이페이지 - 프로젝트별 task
    @GetMapping("/project-tasks")
    public ResponseEntity<List<ProjectByTaskDto>> selectProjectByTask(
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok(memberService.selectProjectByTask(customUserDetails.getId()));
    }


    // 다른 회원정보 조회
    @PostMapping("/search-members")
    public ResponseEntity<MemberDto> selectMemberByEmail(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestBody SearchEmail searchEmail) {

        return ResponseEntity.ok(
            memberService.selectMemberByEmail(customUserDetails.getId(), searchEmail));
    }
}
