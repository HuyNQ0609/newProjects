package com.vsafe.admin.server.core.security.custom;

import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.helpers.enums.responseStatus.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Returns a 401 error code (Unauthorized) to the client.
 */
@Component
@Slf4j
public class VSafeAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        Authentication auth
                = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            log.warn("User: " + auth.getName()
                    + " attempted to access the protected URL: "
                    + request.getRequestURI());
        } else log.warn("Pre-authenticated entry point called. Rejecting access");

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        BaseResponse.buildResponse(response, ErrorCode.AuthErrorCode.AUTH9999);
    }
}
