package com.vsafe.admin.server.helpers.constants.enums;

public enum LoginType {
    USER_PASSWORD;

    public boolean isUserPassword() {
        return name().equals(USER_PASSWORD.name());
    }
}

