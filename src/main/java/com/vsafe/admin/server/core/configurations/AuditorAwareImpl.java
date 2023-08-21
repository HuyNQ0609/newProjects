package com.vsafe.admin.server.core.configurations;

import com.vsafe.admin.server.helpers.utils.AssertUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    private TokenStore tokenStore;
    public AuditorAwareImpl(TokenStore tokenStore) {
        AssertUtils.defaultNotNull(tokenStore);
        this.tokenStore = tokenStore;
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
            return null;
        }

        OAuth2AuthenticationDetails auth2AuthenticationDetails = (OAuth2AuthenticationDetails)authentication.getDetails();
        Map<String, Object> details = this.tokenStore.readAccessToken(auth2AuthenticationDetails.getTokenValue()).getAdditionalInformation();
        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) details.get("userInfo");

        return Optional.ofNullable(data.get("id").toString());
    }
}
