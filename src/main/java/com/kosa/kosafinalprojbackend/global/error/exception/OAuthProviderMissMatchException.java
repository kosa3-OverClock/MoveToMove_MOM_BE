package com.kosa.kosafinalprojbackend.global.error.exception;

import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;

public class OAuthProviderMissMatchException extends CustomBaseException {
    public OAuthProviderMissMatchException(ResponseCode responseCode) {
        super(responseCode);
    }
}