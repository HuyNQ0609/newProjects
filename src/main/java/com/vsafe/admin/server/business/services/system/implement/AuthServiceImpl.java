package com.vsafe.admin.server.business.services.system.implement;

import com.vsafe.admin.server.business.entities.operation.CustomerEntity;
import com.vsafe.admin.server.business.repositories.cms.operation.ICustomerRepositoryJpa;
import com.vsafe.admin.server.business.request.system.ChangePasswordRequest;
import com.vsafe.admin.server.business.request.system.CreateUserRequest;
import com.vsafe.admin.server.business.request.system.LoginRequest;
import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.business.services.system.AuthService;
import com.vsafe.admin.server.core.configurations.properties.OAuth2Properties;
import com.vsafe.admin.server.core.exceptions.SystemException;
import com.vsafe.admin.server.helpers.constants.AuthorityConstant;
import com.vsafe.admin.server.helpers.enums.responseStatus.ResponseStatus;
import com.vsafe.admin.server.helpers.utils.AssertUtils;
import com.vsafe.admin.server.helpers.utils.AuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final ICustomerRepositoryJpa iCustomerRepositoryJpa;

    public AuthServiceImpl(ICustomerRepositoryJpa iCustomerRepositoryJpa, RestTemplate restTemplate, OAuth2Properties oAuth2Properties, ICustomerRepositoryJpa userRepo) {
        this.iCustomerRepositoryJpa = iCustomerRepositoryJpa;
        AssertUtils.defaultNotNull(restTemplate);
        AssertUtils.defaultNotNull(oAuth2Properties);
        AssertUtils.defaultNotNull(userRepo);
        this.restTemplate = restTemplate;
        this.oAuth2Properties = oAuth2Properties;
        this.userRepo = userRepo;

    }

    @Override
    public ResponseEntity login(LoginRequest loginRequest) {
        return execute(loginRequest.getUserName(), loginRequest.getPassword(), null);
    }

    @Override
    public BaseResponse logout(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, AuthorityConstant.BEARER + " " + token);
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<String> request = new HttpEntity<>(headers);
            String logoutUrl = oAuth2Properties.getUrl().getLogout();
            ResponseEntity<BaseResponse> response =
                    restTemplate.postForEntity(logoutUrl, request, BaseResponse.class);
            return response.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SystemException();
        }
    }

    @Override
    public ResponseEntity refreshToken(String refreshToken) {
        return execute(null, null, refreshToken);
    }

    @Override
    public BaseResponse changePassword(ChangePasswordRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, AuthorityConstant.BEARER + " " + request.getToken());
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<ChangePasswordRequest> requestAuth = new HttpEntity<>(request, headers);
            String changePassUrl = oAuth2Properties.getUrl().getChangePassword();
            ResponseEntity<BaseResponse> response =
                    restTemplate.postForEntity(changePassUrl, requestAuth, BaseResponse.class);
            return response.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SystemException();
        }
    }

    @Override
    public BaseResponse addUser(CreateUserRequest request) {
        try {
            /*CustomerEntity customerEntity = iCustomerRepositoryJpa.findById(request.getCustomerId()).orElse(null);
            if (Objects.isNull(customerEntity)) {
                return BaseResponse.error("Khách hàng không tồn tại");
            }*/
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, AuthorityConstant.BEARER + " " + request.getToken());
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<CreateUserRequest> requestAuth = new HttpEntity<>(request, headers);
            String addUserUrl = oAuth2Properties.getUrl().getAddUser();
            ResponseEntity<BaseResponse> response =
                    restTemplate.postForEntity(addUserUrl, requestAuth, BaseResponse.class);
            return response.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SystemException();
        }
    }

    @Override
    public BaseResponse updateUserName(CreateUserRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, AuthorityConstant.BEARER + " " + request.getToken());
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<CreateUserRequest> requestAuth = new HttpEntity<>(request, headers);
            String addUserUrl = oAuth2Properties.getUrl().getUpdateUsername();
            ResponseEntity<BaseResponse> response =
                    restTemplate.postForEntity(addUserUrl, requestAuth, BaseResponse.class);
            return response.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SystemException();
        }
    }

    private ResponseEntity execute(String usr, String pwd, String refreshToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            String clientEncoder = oAuth2Properties.getClientId() + ":" + oAuth2Properties.getClientSecret();
            String clientCredentials = Base64.getEncoder().encodeToString(clientEncoder.getBytes());
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add(HttpHeaders.AUTHORIZATION, AuthorityConstant.BASIC + " " + clientCredentials);
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            MultiValueMap<String, String> params = AuthUtils.getParams(usr, pwd, refreshToken);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            String loginUrl = oAuth2Properties.getUrl().getLogin();
            return restTemplate.postForEntity(loginUrl, request, Object.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SystemException();
        }
    }

    private final RestTemplate restTemplate;
    private final OAuth2Properties oAuth2Properties;

    private final ICustomerRepositoryJpa userRepo;
}
