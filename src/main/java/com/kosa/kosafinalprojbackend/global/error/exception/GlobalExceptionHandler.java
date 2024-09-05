package com.kosa.kosafinalprojbackend.global.error.exception;

import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomBaseException.class)
  public ResponseEntity<Object> handleCustomBaseException(CustomBaseException ex) {
    ResponseCode responseCode = ex.getResponseCode();

    return new ResponseEntity<>(responseCode.toJson(), responseCode.getStatus());
  }
}
