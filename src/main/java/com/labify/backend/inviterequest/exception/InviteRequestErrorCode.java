package com.labify.backend.inviterequest.exception;

import com.labify.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum InviteRequestErrorCode implements ErrorCode {

    // 400 오류
    INVALID_FACILITY_CODE(40010, HttpStatus.BAD_REQUEST, "유효하지 않은 시설 코드입니다."),

    // 404 오류
    REQUEST_NOT_FOUND(40410, HttpStatus.NOT_FOUND, "해당 요청을 찾을 수 없습니다."),

    // 409 오류 (충돌)
    ALREADY_PROCESSED(40910, HttpStatus.CONFLICT, "이미 처리된 요청입니다."),
    ALREADY_REQUESTED(40911, HttpStatus.CONFLICT, "이미 처리된 요청입니다.");

    private final int code;
    private final HttpStatus status;
    private final String message;
}
