package com.vsafe.admin.server.helpers.constants.enums.subcriptions;

import com.vsafe.admin.server.helpers.enums.CommonEnum;

public enum FrequencyType implements CommonEnum<String, String> {
    LIFE_TIME(Constants.LIFE_TIME, "Trọn đời"),
    MONTH(Constants.MONTH, "Tháng"),
    YEAR(Constants.YEAR, "Năm");

    public static class Constants {
        public static final String MONTH = "MONTH";
        public static final String YEAR = "YEAR";
        public static final String LIFE_TIME = "LIFE_TIME";

    }
    private final String value;
    private final String description;

    FrequencyType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.description;
    }
}
