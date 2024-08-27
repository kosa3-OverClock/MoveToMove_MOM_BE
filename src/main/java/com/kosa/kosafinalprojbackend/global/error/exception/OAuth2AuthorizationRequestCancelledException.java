package com.kosa.kosafinalprojbackend.global.error.exception;

import org.springframework.security.core.AuthenticationException;

public class OAuth2AuthorizationRequestCancelledException extends AuthenticationException {

    public OAuth2AuthorizationRequestCancelledException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public OAuth2AuthorizationRequestCancelledException(String msg) {
        super(msg);
    }
}
