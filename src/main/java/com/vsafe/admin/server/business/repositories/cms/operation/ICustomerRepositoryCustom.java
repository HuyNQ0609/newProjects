package com.vsafe.admin.server.business.repositories.cms.operation;

import com.vsafe.admin.server.business.entities.operation.CustomerEntity;
import com.vsafe.admin.server.business.entities.system.MenuEntity;
import com.vsafe.admin.server.business.request.operation.SearchCustomerRequest;
import com.vsafe.admin.server.business.request.system.SearchMenuRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface ICustomerRepositoryCustom {

    long countSearch(SearchCustomerRequest request, String clientId);

    List<CustomerEntity> searchWithPaging(SearchCustomerRequest request, String clientId);

    List<CustomerEntity> searchWithoutPaging(SearchCustomerRequest request, String clientId);


    List<CustomerEntity> searchByPhoneNumber(String phoneNumber, String clientId);
    List<CustomerEntity> searchByEmail(String email, String clientId);
    List<CustomerEntity> searchByIdentityNumber(String identityNumber, String clientId);

    List<CustomerEntity> findByParentIdAndClientId(String parentId, String clientId);
}
