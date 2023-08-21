package com.vsafe.admin.server.business.services.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsafe.admin.server.business.entities.operation.CustomerEntity;
import com.vsafe.admin.server.business.entities.system.EmployeeEntity;
import com.vsafe.admin.server.business.repositories.cms.operation.ICustomerRepositoryJpa;
import com.vsafe.admin.server.business.repositories.cms.system.IEmployeeRepositoryJpa;
import com.vsafe.admin.server.business.response.operation.CustomerResponse;
import com.vsafe.admin.server.business.response.system.EmployeeResponse;
import com.vsafe.admin.server.business.response.system.UserDetailResponse;
import com.vsafe.admin.server.core.exceptions.NotFoundException;
import com.vsafe.admin.server.helpers.enums.UserType;
import com.vsafe.admin.server.helpers.utils.AssertUtils;
import com.vsafe.admin.server.helpers.utils.mapper.GsonParserUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import com.vsafe.admin.server.core.exceptions.SystemException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class UserDetailService {
    private final TokenStore tokenStore;

    private final ICustomerRepositoryJpa customerRepository;

    private final IEmployeeRepositoryJpa employeeRepository;

    public UserDetailService(TokenStore tokenStore, ICustomerRepositoryJpa customerRepository, IEmployeeRepositoryJpa employeeRepository) {
        AssertUtils.defaultNotNull(tokenStore);
        AssertUtils.defaultNotNull(customerRepository);
        AssertUtils.defaultNotNull(employeeRepository);
        this.tokenStore = tokenStore;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
    }

    public UserDetailResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails auth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        Map<String, Object> details = this.tokenStore.readAccessToken(auth2AuthenticationDetails.getTokenValue()).getAdditionalInformation();
        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) details.get("userInfo");
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(data);
            UserDetailResponse userDetailResponse = GsonParserUtils.parseStringToObject(json, UserDetailResponse.class);
            if (userDetailResponse == null) throw new NotFoundException("Không tìm thấy thông tin user");
            if (UserType.INTERNAL.isEqual(userDetailResponse.getUserType())) {
                EmployeeEntity employee = employeeRepository.findByUserName(userDetailResponse.getUserName()).orElseThrow(
                        () -> new NotFoundException("Không tìm thấy thông tin nhân viên.")
                );
                userDetailResponse.setEmployeeInfo(EmployeeResponse.of(employee));
            } else if (UserType.CUSTOMER.isEqual(userDetailResponse.getUserType())) {
                CustomerEntity customer = customerRepository.findByUserName(userDetailResponse.getUserName()).orElseThrow(
                        () -> new NotFoundException("Không tìm thấy thông tin khách hàng.")
                );
                userDetailResponse.setCustomerInfo(CustomerResponse.of(customer));
            }
            return userDetailResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
