package com.vsafe.admin.server.core.filter;

import com.vsafe.admin.server.business.request.BaseRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

@Component
@Slf4j
public class LogUrlFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String requestId = UUID.randomUUID().toString();
        servletRequest.setAttribute(BaseRequest.REQUEST_ID, requestId);
        logRequest((HttpServletRequest) servletRequest, requestId);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    private void logRequest(HttpServletRequest request, String requestId) {
        if (request != null) {
            StringBuilder data = new StringBuilder();
            data.append("[LOGGING REQUEST]:")
                    .append("[REQUEST-ID]: ").append(requestId)
                    .append("[PATH]: ").append(request.getRequestURI())
                    .append("[QUERIES]: ").append(request.getQueryString())
                    .append("[HEADERS]: ");

            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                data.append("---").append(key).append(" : ").append(value);
            }
            data.append("[END LOGGING REQUEST]");

            log.info(data.toString());
        }
    }
}
