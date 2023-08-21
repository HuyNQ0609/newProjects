package com.vsafe.admin.server.business.services.opearation;

import com.vsafe.admin.server.business.request.operation.CustomerDetailRequest;
import com.vsafe.admin.server.business.request.operation.SearchCustomerRequest;
import com.vsafe.admin.server.business.request.operation.SearchMarkerRequest;
import com.vsafe.admin.server.business.response.BaseResponse;

public interface GgMapService {
    BaseResponse getGroupArea();
    BaseResponse searchWithoutPaging(SearchMarkerRequest request);
    BaseResponse getListLocation(String areaId);
    BaseResponse getListDevice(String markerId);

    BaseResponse getListDeviceStatusTopRank();

}
