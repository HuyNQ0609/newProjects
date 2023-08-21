package com.vsafe.admin.server.core.configurations;

public class ResourcePath {
    public static final String FIX = "/**";
    public static final String VERSION = "/v1";
    public static final String BASE = ResourcePath.VERSION;
    public static final String TEST = "/test" + ResourcePath.VERSION;
    public static final String PUBLIC = ResourcePath.BASE + "/vsafe";

    public static final String[] NON_LOGGING_URL_LIST = {
            BASE + "/authentication/login",
            BASE + "/authentication/cms-login",
            BASE + "/authentication/change-password",
    };
}
