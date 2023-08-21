package com.vsafe.admin.server.business.services.system;

import com.vsafe.admin.server.business.request.system.EmployeeDetailRequest;
import com.vsafe.admin.server.business.request.system.SearchEmployeeRequest;
import com.vsafe.admin.server.business.response.BaseResponse;

public interface EmployeeService {
    BaseResponse searchWithPaging(SearchEmployeeRequest request);

    BaseResponse updateInfo(EmployeeDetailRequest request);

    BaseResponse insertInfo(EmployeeDetailRequest request);

    BaseResponse saveInfoStatus(EmployeeDetailRequest request);
}
