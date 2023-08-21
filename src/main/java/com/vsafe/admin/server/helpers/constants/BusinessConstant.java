package com.vsafe.admin.server.helpers.constants;

public class BusinessConstant {
    public static class FireFighterLevel {
        public static final Long PROVINCE = 1L;
        public static final Long DISTRICT = 2L;
    }

    public static class DELETED_VALUE {
        public static final Integer NOT_DEL = 0;
        public static final Integer DEL_ED = 1;
    }

    public static class Status {
        public static final Integer ACTIVE = 1;
        public static final Integer DEACTIVE = 0;
    }

    /**
     * Các loại tài khoản của Khách hàng
     * Main_Acc: Tài khoản chính, Extra_Acc: Tài khoản phụ
     */
    public static class CUSTOMER_ACCOUNT {
        public static final Integer MAIN_ACC = 1;
        public static final Integer EXTRA_ACC = 0;
    }

    /**
     * Phương thức xác thực Tài khoản đăng nhập
     */
    public static class VERIFICATION_TYPE {
        public static final String SMS = "SMS";
        public static final String EMAIL = "EMAIL";
    }

    /**
     * Trạng tha của thiết bị Sensor
     */
    public static class SENSOR_STATUS {
        public static final String SMS = "SMS";
        public static final String EMAIL = "EMAIL";
    }

    public static class DEVICE_TYPE {
        public static final String GATEWAY = "GW";
        public static final String SENSOR_NHIET = "CBN";
        public static final String SENSOR_TIA_LUA = "TL";
        public static final String SENSOR_KHOI = "CBK";
    }
}