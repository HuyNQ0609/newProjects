package com.vsafe.admin.server.business.repositories.cms.system;

import com.vsafe.admin.server.business.entities.operation.CustomerEntity;
import com.vsafe.admin.server.business.entities.system.EmployeeEntity;
import com.vsafe.admin.server.business.request.system.SearchEmployeeRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface IEmployeeRepositoryCustom {

    List<EmployeeEntity> searchWithPaging(SearchEmployeeRequest request, String clientId);

    long countSearch(SearchEmployeeRequest request, String clientId);
    }
