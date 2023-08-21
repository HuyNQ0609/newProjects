package com.vsafe.admin.server.business.request;

import com.vsafe.admin.server.helpers.constants.SystemConstants;
import lombok.Data;
import lombok.ToString;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Data
@ToString
public class BaseRequest {
    public static final String REQUEST_ID = "request_id";

    private String requestId = UUID.randomUUID().toString();

    private String requestDate = new SimpleDateFormat(SystemConstants.Date.BASE_DATE_FULL_TIME_FORMAT).format(new Date());
}
