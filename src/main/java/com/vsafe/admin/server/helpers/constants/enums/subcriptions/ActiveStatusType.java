package com.vsafe.admin.server.helpers.constants.enums.subcriptions;

import com.vsafe.admin.server.helpers.enums.CommonEnum;

public enum ActiveStatusType implements CommonEnum<Integer, String> {
    ACTIVE(Constants.ACTIVE, "Active"),
    INACTIVE(Constants.INACTIVE, "InActive");
    public static class Constants {
        public static final Integer ACTIVE = 1;
        public static final Integer INACTIVE = 0;
    }
    private final Integer value;
    private final String description;

    ActiveStatusType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.description;
    }
}
