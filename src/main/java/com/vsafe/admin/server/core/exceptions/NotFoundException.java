package com.vsafe.admin.server.core.exceptions;

import com.vsafe.admin.server.helpers.enums.responseStatus.ResponseStatus;

public class NotFoundException extends SystemException {
    private static final long serialVersionUID = -7431810238117322293L;

    private final static ResponseStatus ERROR_TYPE = ResponseStatus.NOT_FOUND;

    public NotFoundException() {
        super(ERROR_TYPE);
    }

    public NotFoundException(String msg) {
        super(ERROR_TYPE, msg);
    }

}
