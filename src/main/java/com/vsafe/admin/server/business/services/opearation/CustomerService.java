package com.vsafe.admin.server.business.services.opearation;

import com.vsafe.admin.server.business.request.operation.CustomerDetailRequest;
import com.vsafe.admin.server.business.request.operation.SearchCustomerRequest;
import com.vsafe.admin.server.business.response.BaseResponse;

public interface CustomerService {

    BaseResponse searchWithPaging(SearchCustomerRequest request);

    BaseResponse searchWithoutPaging(SearchCustomerRequest request);

    BaseResponse getDetail(String id);

    byte[] export(SearchCustomerRequest request);

    BaseResponse updateInfo(CustomerDetailRequest request);

    BaseResponse updateMemberInfo(CustomerDetailRequest request);

    BaseResponse addMemberInfo(CustomerDetailRequest request);

    BaseResponse saveInfoStatus(CustomerDetailRequest request);

    BaseResponse saveTwoFactor(CustomerDetailRequest request);

    BaseResponse addCustomer(CustomerDetailRequest request);

    BaseResponse listByParent(String id);
}
