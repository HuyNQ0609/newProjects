package com.vsafe.admin.server.helpers.enums;

public enum BaseStatus implements CommonEnum<Integer, String>{
    ACTIVE(1, "Hoạt động"),
    INACTIVE(1, "Khóa"),
    ;
    private String name;
    private Integer value;

    BaseStatus(Integer value, String name) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }
}
