package com.vsafe.admin.server.core.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import java.util.Map;


@JsonSerialize(using = CustomOauthExceptionSerializer.class)
public class CustomOauthException extends OAuth2Exception {

    private static final long serialVersionUID = 1L;

    public CustomOauthException(String msg) {
        super(msg);
    }

    public CustomOauthException(String msg, Map<String, String> valueAdditionalInformation) {
        super(msg);
        if (valueAdditionalInformation != null) {
            for (Map.Entry<String, String> entry : valueAdditionalInformation.entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                this.addAdditionalInformation(key, add);
            }
        }

    }

}
