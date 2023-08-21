package com.vsafe.admin.server.core.security;

import com.vsafe.admin.server.core.configurations.ResourcePath;
import com.vsafe.admin.server.core.configurations.properties.OAuth2Properties;
import com.vsafe.admin.server.core.security.custom.AccessDeniedHandler;
import com.vsafe.admin.server.core.security.custom.CorsConfigurationSource;
import com.vsafe.admin.server.core.security.custom.VSafeAuthenticationEntryPoint;
import com.vsafe.admin.server.core.security.exception.CustomWebResponseExceptionTranslator;
import com.vsafe.admin.server.helpers.utils.AssertUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
@Order(1)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String OPEN_URL = getURL(ResourcePath.PUBLIC);
    private static final String TEST_URL = getURL(ResourcePath.TEST);


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(oAuth2Properties.getResourceId()).stateless(false);
        resources.tokenStore(tokenStore());
        resources.tokenServices(tokenService());
    }

    // To allow the ResourceServerConfigurerAdapter to understand the token,
    // it must share the same characteristics with AuthorizationServerConfigurerAdapter.
    // So, we must wire it up the beans in the ResourceServerSecurityConfigurer.
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(oAuth2Properties.getJwtSingingKey());
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Primary
    @Bean
    public RemoteTokenServices tokenService() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(oAuth2Properties.getUrl().getCheckToken());
        tokenService.setClientId(oAuth2Properties.getClientId());
        tokenService.setClientSecret(oAuth2Properties.getClientSecret());
        return tokenService;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                // CORS
                .cors().configurationSource(corsConfigurationSource)
                .and()
                // CSRF
                // .csrf().ignoringAntMatchers().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .authorizeRequests()
                .antMatchers(TEST_URL + "/test-cms").hasAnyAuthority("ADMIN")
                .antMatchers(TEST_URL + "/test-partner").hasAnyAuthority("PARTNER")
                .antMatchers("/v1/socket/**").permitAll()
                .antMatchers("/v1/web-socket/**").permitAll()
                .antMatchers("/v1/authentication/login").permitAll()
                .antMatchers("/v1/authentication/cms-login").permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private static String getURL(CharSequence element) {
        return String.join("", ResourcePath.FIX, element,
                ResourcePath.FIX);
    }

    private final OAuth2Properties oAuth2Properties;
    private final CorsConfigurationSource corsConfigurationSource;
    private final VSafeAuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final CustomWebResponseExceptionTranslator webResponseExceptionTranslator;

    public ResourceServerConfig(OAuth2Properties oAuth2Properties,
                                CorsConfigurationSource corsConfigurationSource,
                                VSafeAuthenticationEntryPoint authenticationEntryPoint,
                                AccessDeniedHandler accessDeniedHandler,
                                CustomWebResponseExceptionTranslator webResponseExceptionTranslator) {
        AssertUtils.defaultNotNull(corsConfigurationSource);
        AssertUtils.defaultNotNull(authenticationEntryPoint);
        AssertUtils.defaultNotNull(accessDeniedHandler);
        AssertUtils.defaultNotNull(webResponseExceptionTranslator);
        AssertUtils.defaultNotNull(oAuth2Properties);
        this.webResponseExceptionTranslator = webResponseExceptionTranslator;
        this.corsConfigurationSource = corsConfigurationSource;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.oAuth2Properties = oAuth2Properties;

    }
}
