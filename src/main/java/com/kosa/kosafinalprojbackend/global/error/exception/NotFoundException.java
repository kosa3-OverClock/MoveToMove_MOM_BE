package com.kosa.kosafinalprojbackend.global.error.exception;

import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;

public class NotFoundException extends CustomBaseException{
    public NotFoundException(ResponseCode responseCode){
        super(responseCode.getMessage(), responseCode);
    }
}
