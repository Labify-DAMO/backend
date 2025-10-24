package com.labify.backend.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getStatus();
    int getCode();
    String getMessage();
}
