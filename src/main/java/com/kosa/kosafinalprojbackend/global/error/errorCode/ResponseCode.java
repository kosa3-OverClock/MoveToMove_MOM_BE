package com.kosa.kosafinalprojbackend.global.error.errorCode;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT) // JSON 으로 전달하기 위함
public enum ResponseCode {

    // 사용자
    LOGIN_SUCCESS(OK, "로그인하셨습니다."),
    UNABLE_LOGIN(BAD_REQUEST, "아이디 또는 비밀번호가 잘못 되었습니다."),

    MEMBER_CREATED(CREATED, "회원가입이 완료 되었습니다."),
    EXISTS_EMAIL(CONFLICT, "이미 존재하는 이메일입니다."),
    EXISTS_NICKNAME(CONFLICT, "이미 존재하는 닉네임입니다."),
    JSON_PARSE_ERROR(BAD_REQUEST, "데이터를 처리하는 중에 오류가 발생했습니다."),
    MEMBER_MODIFY_SUCCESS(OK, "회원정보를 수정했습니다."),
    NOT_FOUND_MEMBER(NOT_FOUND, "회원정보를 찾을 수 없습니다."),
    MEMBER_DELETE(OK, "회원 탈퇴 처리되었습니다."),

    // 토큰
    NOT_FIND_TOKEN(BAD_REQUEST,"토큰이 유효하지 않습니다."),
    ISSUE_ACCESS_TOKEN(OK, "토큰 발급"),

    // 소셜 로그인
    ERR_SOCIAL_PROVIDER_MISMATCH(BAD_REQUEST, "Social Domain이 저장된 값과 다릅니다."),
    OAUTH2_AUTHENTICATION_ERROR(UNAUTHORIZED, "OAuth2 인증 중 오류가 발생했습니다."),
    OAUTH2_PROVIDER_ERROR(SERVICE_UNAVAILABLE, "OAuth2 공급자와 통신하는 중에 오류가 발생했습니다."),
    OAUTH2_TOKEN_EXPIRED(UNAUTHORIZED, "OAuth2 액세스 토큰이 만료되었습니다."),
    OAUTH2_INVALID_GRANT(UNAUTHORIZED, "유효하지 않은 인증 코드입니다."),
    OAUTH2_LOGIN_CANCELLED(FORBIDDEN, "사용자가 로그인 요청을 취소했습니다."),
    OAUTH2_INVALID_CLIENT_REGISTRATION(HttpStatus.BAD_REQUEST,"Invalid OAuth2 Client Registration ID. 유효하지 않은 소셜 로그인 시도입니다."),

    // 프로젝트
    PROJECT_CREATE(CREATED, "프로젝트가 생성되었습니다."),
    NO_PROJECT_LEADER(NOT_FOUND, "프로젝트 팀장이 아닙니다."),
    PROJECT_MODIFY(OK, "프로젝트가 정보가 수정되었습니다."),
    NOT_FOUND_PROJECT(NOT_FOUND,"프로젝트 정보를 찾을 수 없습니다."),
    // 컬럼
    NOT_FOUND_COLUMN(NOT_FOUND, "칸반 컬럼이 존재하지 않습니다."),
    COLUMN_DELETE(OK,"칸반 컬럼이 삭제되었습니다."),
    KANBAN_COLUMN_UPDATE(OK,"컬럼이 수정 완료 되었습니다."),
    // 칸반 컬럼
    NOT_FOUND_KANBAN_COLUMN(NOT_FOUND, "존재하지 않는 칸반 컬럼입니다."),

    // 칸반 카드
    KANBAN_CARD_CREATED(CREATED ,"칸반 카드가 생성 되었습니다."),
    KANBAN_CARD_MODIFY_SUCCESS(OK, "칸반 카드 정보를 수정했습니다."),
    NOT_FOUND_KANBAN_CARD(NOT_FOUND, "존재하지 않는 칸반 카드 입니다."),

    // 공통
    NOT_FOUND_ID(NOT_FOUND, "존재하지 않는 아이디입니다."),

    YES_PROJECT_LEADER(CONFLICT, "프로젝트 중 팀장인 프로젝트가 있습니다.");


    private final HttpStatus status;
    private final String message;
    private Object data;

    public ResponseCode withData(Object data) {
        this.data = data;
        return this;
    }


    @JsonValue
    public Object toJson() {
        return new ResponseCodeJson(status.value(), message, data);
    }

    private record ResponseCodeJson(int status, String message, Object data) {
    }
}
