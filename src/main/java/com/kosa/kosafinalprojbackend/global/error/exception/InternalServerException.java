package com.kosa.kosafinalprojbackend.global.error.exception;


import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;

public class InternalServerException extends CustomBaseException {
    public InternalServerException(ResponseCode responseCode){
        super(responseCode.getMessage(), responseCode);
    }
}