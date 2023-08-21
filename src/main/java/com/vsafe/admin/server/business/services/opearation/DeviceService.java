package com.vsafe.admin.server.business.services.opearation;

import com.vsafe.admin.server.business.response.BaseResponse;

public interface DeviceService {
    BaseResponse searchByCustomer(String customerId);
}
