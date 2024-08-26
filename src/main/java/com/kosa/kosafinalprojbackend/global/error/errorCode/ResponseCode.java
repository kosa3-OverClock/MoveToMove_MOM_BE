package com.kosa.kosafinalprojbackend.global.error.errorCode;

import static org.springframework.http.HttpStatus.*;

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
    MEMBER_CREATED(CREATED, "회원가입이 완료 되었습니다."),
    EXISTS_EMAIL(CONFLICT, "이미 존재하는 이메일입니다."),
    EXISTS_NICKNAME(CONFLICT, "이미 존재하는 닉네임입니다."),
    JSON_PARSE_ERROR(BAD_REQUEST, "데이터를 처리하는 중에 오류가 발생했습니다."),


//    // A : 100 , B : 200, C : 300, D : 400, E : 500
//    MEMBER_CREATED(HttpStatus.CREATED, "B01", "가입 되셨습니다."),
//    MEMBER_UPDATED(HttpStatus.OK, "B00", "수정 완료"),
//    MEMBER_DELETED(HttpStatus.OK, "B00", "회원님의 정보가 무사히 삭제되었습니다."),
//    MEMBER_ALL_DELETED(HttpStatus.OK, "B00", "모든 회원의 데이터가 삭제 되었습니다."),
//    // running
//    RUN_START(HttpStatus.OK, "B00", "런닝이 시작되었습니다."),
//    RUN_END(HttpStatus.OK, "B00", "런닝이 끝났습니다."),
//
//    // Auth
//    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "D90", "인증 정보가 없는 요청입니다"),
//    TOKEN_INVALID(HttpStatus.BAD_REQUEST, "D91", "유효하지 않은 토큰입니다"),
//    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "D92", "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
//    PASSWORD_INVALID(HttpStatus.BAD_REQUEST, "D93", "비밀번호가 올바르지 않습니다."),
//    UNAUTHORIZED_ACTION(HttpStatus.UNAUTHORIZED, "D97", "허가되지 않은 행동입니다."),
//
//    // User
//    ALREADY_EXIST(HttpStatus.BAD_REQUEST, "D94", "이미 존재하는 사용자입니다."),
//
//    // Common
    INVALID_INPUT_VALUE(BAD_REQUEST, "잘못된 구문 요청입니다."),
    AUTHENTICATION_FAILED(UNAUTHORIZED, "지정한 리소스에 대한 액세스 권한이 없습니다."),
    PAYMENT_REQUIRED(HttpStatus.PAYMENT_REQUIRED, "리소스에 액세스 하기 위해서는 결제가 필요합니다."),
    ACCESS_DENIED(FORBIDDEN, "지정한 리소스에 대한 액세스는 금지되었습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "대상 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메소드 호출 : 요청한 URI 가 지정한 메소드를 지원하지 않습니다."),
    NOT_VALID_JSON(PRECONDITION_FAILED, "JSON 형식을 기대했지만, JSON 형식으로 변환할 수 없는 값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다."),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "게이트웨이 또는 프록시 서버가 잘못된 응답을 받았습니다."),
//    // -------------------------------- 여기부턴 커스텀 에러 -----------------------------------------
//    // 커스텀 예외 생성시 code 90번부터 지정 -> ex) REQUIRE_MORE_COFFEE(HttpStatus.SERVICE_UNAVAILABLE, "E90", "더 많은 커피가 필요합니다.")
//    REQUIRE_MORE_COFFEE(HttpStatus.SERVICE_UNAVAILABLE, "E90", "커피가 부족 합니다.")
    ;

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
