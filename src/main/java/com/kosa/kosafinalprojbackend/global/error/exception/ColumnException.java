package com.kosa.kosafinalprojbackend.global.error.exception;

import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;

public class ColumnException extends CustomBaseException {
    public ColumnException(String message, ResponseCode responseCode) {
        super(message, responseCode);
    }

    public ColumnException(ResponseCode responseCode) {
        super(responseCode);
    }
}
