package com.vsafe.admin.server.core.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RequestLoggingFilterConfig {
    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }

    @Bean
    public FilterRegistrationBean<LogUrlFilter> filterRegistrationBean() {
        FilterRegistrationBean<LogUrlFilter> registrationBean = new FilterRegistrationBean<>();
        LogUrlFilter customURLFilter = new LogUrlFilter();

        registrationBean.setFilter(customURLFilter);
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
