package com.justet.shiftlabtest.core.exception;

import com.justet.shiftlabtest.api.error.ErrorCode;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private final ErrorCode errorCode;

    public ServiceException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}