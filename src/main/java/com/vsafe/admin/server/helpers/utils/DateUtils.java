package com.vsafe.admin.server.helpers.utils;

import java.sql.Timestamp;
import java.util.Calendar;

public class DateUtils {
    public static Timestamp createTimestamp() {
        Calendar calendar = Calendar.getInstance();
        return new Timestamp((calendar.getTime()).getTime());
    }
}
