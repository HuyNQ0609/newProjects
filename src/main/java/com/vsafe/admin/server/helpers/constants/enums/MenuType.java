package com.vsafe.admin.server.helpers.constants.enums;

import com.vsafe.admin.server.helpers.enums.CommonEnum;

// Để dang Enum để phục vụ validate
public enum MenuType implements CommonEnum {
    HEADER(Constants.HEADER, "Menu trên Header"),
    SIDE_APP(Constants.SIDE_APP, "Menu App"),
    SIDE_ADMIN(Constants.SIDE_ADMIN, "Menu Admin");

    public static class Constants {
        public static final Integer HEADER = 0;
        public static final Integer SIDE_APP = 1;
        public static final Integer SIDE_ADMIN = 2;
    }

    private final Integer value;
    private final String description;

    MenuType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public Object getName() {
        return this.description;
    }
}
