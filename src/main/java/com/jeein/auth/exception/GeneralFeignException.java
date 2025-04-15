package com.jeein.auth.exception;

import lombok.Getter;

@Getter
public class GeneralFeignException extends RuntimeException {
    private final ErrorCode errorCode;

    public GeneralFeignException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
