package com.vsafe.admin.server.core.exceptions;

import com.vsafe.admin.server.helpers.enums.responseStatus.ResponseStatus;

import java.io.Serializable;

public class SystemException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 3327217472962035232L;

    private final ResponseStatus errorType;

    public SystemException(ResponseStatus type, Throwable t, String msg) {
        super(msg, t);
        this.errorType = type;
    }

    public SystemException(ResponseStatus type, String msg) {
        super(msg);
        this.errorType = type;
    }

    public SystemException(ResponseStatus type) {
        super(type.message());
        this.errorType = type;
    }

    public SystemException(String msg) {
        super(msg);
        this.errorType = ResponseStatus.SYSTEM_ERROR;
    }

    public SystemException() {
        this(ResponseStatus.SYSTEM_ERROR);
    }

    private ResponseStatus obtainErrorType() {
        if (errorType == null) {
            return ResponseStatus.SYSTEM_ERROR;
        }
        return errorType;
    }

    public ResponseStatus getErrorType() {
        return obtainErrorType();
    }

    public String getErrorCode() {
        return obtainErrorType().messageCode();
    }

    public String getErrorMsg() {
        return obtainErrorType().message();
    }

}
