package com.vsafe.admin.server.core.filter;

import com.vsafe.admin.server.core.annotations.HideResponseLog;
import com.vsafe.admin.server.helpers.utils.AssertUtils;
import com.vsafe.admin.server.business.request.BaseRequest;
import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.business.services.common.LoggingService;
import com.vsafe.admin.server.helpers.utils.DateUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;
import java.util.Date;

@ControllerAdvice
public class CustomResponseBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        if (serverHttpRequest instanceof ServletServerHttpRequest &&
                serverHttpResponse instanceof ServletServerHttpResponse) {
            if (showLog(methodParameter)) {
                Object requestId = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getAttribute(BaseRequest.REQUEST_ID);
                if (o instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) o;
                    response.setRequestId(requestId.toString());
                    response.setTimestamp(DateUtils.createTimestamp());
                }
                loggingService.logResponse(
                        ((ServletServerHttpRequest) serverHttpRequest).getServletRequest(),
                        ((ServletServerHttpResponse) serverHttpResponse).getServletResponse(), o);
            } else {
                loggingService.logResponse(
                        ((ServletServerHttpRequest) serverHttpRequest).getServletRequest(),
                        ((ServletServerHttpResponse) serverHttpResponse).getServletResponse(), "**Hide response log**");
            }
        }

        return o;
    }

    private boolean showLog(MethodParameter methodParameter) {
        Annotation[] annotations = methodParameter.getMethodAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(HideResponseLog.class)) return false;
        }
        return true;
    }

    private final LoggingService loggingService;

    public CustomResponseBodyAdviceAdapter(LoggingService loggingService) {
        AssertUtils.defaultNotNull(loggingService);
        this.loggingService = loggingService;
    }
}
