package com.vsafe.admin.server.helpers.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsafe.admin.server.business.response.system.UserDetailResponse;
import com.vsafe.admin.server.helpers.enums.IntentStateWithDescription;
import com.vsafe.admin.server.helpers.utils.mapper.GsonParserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class AuthUtils {
    public static MultiValueMap<String, String> getParams(String usr, String pwd, String refreshToken) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (StringUtils.isNotBlank(usr)) {
            map.add(USERNAME, usr);
        }
        if (StringUtils.isNotBlank(pwd)) {
            map.add(PASSWORD, pwd);
            map.add(GRANT_TYPE, GrantType.PASSWORD.state());
        }
        if (StringUtils.isNotBlank(refreshToken)) {
            map.add(REFRESH_TOKEN, refreshToken);
            map.add(GRANT_TYPE, GrantType.REFRESH_TOKEN.state());
        }
        map.add(SCOPE, READ);
        return map;
    }

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private final static String REFRESH_TOKEN = "refresh_token";
    private static final String GRANT_TYPE = "grant_type";
    private static final String SCOPE = "scope";
    private static final String READ = "read";

    enum GrantType implements IntentStateWithDescription {
        PASSWORD("password"),
        REFRESH_TOKEN("refresh_token"),
        AUTHORIZATION_CODE("authorization_code"),;

        private final String description;

        GrantType(String description) {
            this.description = description;
        }

        @Override public String description() {
            return this.description;
        }

        @Override
        public String state() {
            return StringUtils.lowerCase(name());
        }

        @Override
        public String getValue() {
            return name();
        }
    }
}
