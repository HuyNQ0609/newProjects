package com.vsafe.admin.server.business.services.common;

import com.vsafe.admin.server.business.request.BaseRequest;
import com.vsafe.admin.server.helpers.utils.mapper.GsonParserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
@Slf4j
public class LoggingService {

    public void logRequest(HttpServletRequest httpServletRequest, Object body) {
        Object requestId = httpServletRequest.getAttribute(BaseRequest.REQUEST_ID);

        String data = "[LOGGING REQUEST BODY]:" +
                "[REQUEST-ID]: " + requestId +
                "[BODY REQUEST]: " +
                GsonParserUtils.parseObjectToString(body) +
                "[LOGGING REQUEST BODY]";
        log.info(data);
    }

    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {
        Object requestId = httpServletRequest.getAttribute(BaseRequest.REQUEST_ID);
        String data = "[LOGGING RESPONSE]:" +
                "[REQUEST-ID]: " + requestId +
                "[BODY RESPONSE]: " +
                GsonParserUtils.parseObjectToString(body) +
                "[END LOGGING RESPONSE]";

        log.info(data);
    }

}
