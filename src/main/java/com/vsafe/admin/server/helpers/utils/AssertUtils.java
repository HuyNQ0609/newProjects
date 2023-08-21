package com.vsafe.admin.server.helpers.utils;

public class AssertUtils {

    public static void defaultNotNull(Object obj) {
        final String IS_NULL = " is null";
        org.springframework.util.Assert.notNull(obj, obj.getClass().getSimpleName() + IS_NULL);
    }
}
