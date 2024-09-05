package com.kosa.kosafinalprojbackend.global.error.exception;

import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;

public class JwtValidationException extends CustomBaseException {

  public JwtValidationException(ResponseCode responseCode) {
    super(responseCode);
  }
}
