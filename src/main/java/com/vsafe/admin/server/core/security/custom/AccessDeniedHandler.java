package com.vsafe.admin.server.core.security.custom;

import com.vsafe.admin.server.business.request.BaseRequest;
import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.helpers.enums.responseStatus.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Service
public class AccessDeniedHandler extends AccessDeniedHandlerImpl {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        accessDeniedException.printStackTrace();
        log.info("Pre-authenticated entry point called. Rejecting access");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        Object requestId = request.getAttribute(BaseRequest.REQUEST_ID);
        BaseResponse.buildResponse(response, ErrorCode.AuthErrorCode.AUTH0201, requestId.toString());
    }
}
