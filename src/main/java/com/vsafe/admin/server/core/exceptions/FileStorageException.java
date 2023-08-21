package com.vsafe.admin.server.core.exceptions;


import com.vsafe.admin.server.helpers.enums.responseStatus.ResponseStatus;

public class FileStorageException extends SystemException {
    private static final long serialVersionUID = 1L;

    private final static ResponseStatus ERROR_TYPE = ResponseStatus.UNKNOWN;

    public FileStorageException() {
        super(ERROR_TYPE);
    }

    public FileStorageException(String message) {
        super(ERROR_TYPE, message);
    }

    public FileStorageException(ResponseStatus errorType, String message) {
        super(errorType, message);
    }

    public FileStorageException(ResponseStatus errorType) {
        super(errorType);
    }
}
