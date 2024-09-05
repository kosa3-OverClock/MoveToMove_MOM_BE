package com.kosa.kosafinalprojbackend.global.error.response;

import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

  private String Message;

  public ErrorResponse(final ResponseCode code) {
    this.Message = code.getMessage();
  }

  public ErrorResponse(final ResponseCode code, final String message) {
    this.Message = message;
  }

  public static ErrorResponse of(final ResponseCode code) {
    return new ErrorResponse(code);
  }

  public static ErrorResponse of(final ResponseCode code, final String message) {
    return new ErrorResponse(code, message);
  }
}
