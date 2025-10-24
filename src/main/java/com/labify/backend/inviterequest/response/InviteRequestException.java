package com.labify.backend.inviterequest.response;

import com.labify.backend.common.exception.ErrorCode;
import com.labify.backend.common.response.GeneralException;

public class InviteRequestException extends GeneralException {
    public InviteRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}