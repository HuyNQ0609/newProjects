package com.vsafe.admin.server.core.exceptions;

import com.vsafe.admin.server.helpers.enums.responseStatus.ResponseStatus;

public class BadRequestException extends SystemException {
    private static final long serialVersionUID = -7431810328087316293L;

    private final static ResponseStatus ERROR_TYPE = ResponseStatus.INVALID;

    public BadRequestException() {
        super(ERROR_TYPE);
    }

    public BadRequestException(String message) {
        super(ERROR_TYPE, message);
    }
}
