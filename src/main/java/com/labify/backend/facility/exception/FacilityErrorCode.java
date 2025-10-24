package com.labify.backend.facility.exception; // facility 도메인 하위에 생성

import com.labify.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FacilityErrorCode implements ErrorCode {

    FACILITY_NOT_FOUND(40403, HttpStatus.NOT_FOUND, "해당 시설을 찾을 수 없습니다.");

    private final int code;
    private final HttpStatus status;
    private final String message;
}