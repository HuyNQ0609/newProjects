package com.vsafe.admin.server.helpers.enums.responseStatus;

public enum ResponseStatus implements IResponseStatus {
    SUCCESS("Success"),
    ACCESS_DENIED("Access Denied"),
    NOT_FOUND("Not found"),
    INVALID("Invalid request"),

    UNKNOWN("unknown error."),
    UNIQUE("existed."),
    SYSTEM_ERROR("System Error"),
    UNSUPPORTED_MEDIA_TYPE("unsupported media type"),

    PROCESSING("Other request is still running. Please try again later");

    private String message;

    ResponseStatus(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return message;
    }

    public IResponseStatus setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String messageCode() {
        return this.name();
    }
}
