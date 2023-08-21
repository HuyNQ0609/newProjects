package com.vsafe.admin.server.helpers.enums;

public enum AdministrativeUnitType implements CommonEnum<Integer, String> {
    PROVINCE(Constant.PROVINCE, "Tỉnh/ Thành phố"),
    DISTRICT(Constant.DISTRICT, "Quận/ Huyện"),
    WARD(Constant.WARD, "Phường/ Xã"),
    VILLAGE(Constant.VILLAGE, "Thôn/ Xóm"),
    ;

    public static class Constant {
        public static final Integer PROVINCE = 1;
        public static final Integer DISTRICT = 2;
        public static final Integer WARD = 3;
        public static final Integer VILLAGE = 4;
    }

    AdministrativeUnitType(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    private Integer value;
    private String name;

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }
}
