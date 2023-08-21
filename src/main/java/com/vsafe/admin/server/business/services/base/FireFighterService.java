package com.vsafe.admin.server.business.services.base;


import com.vsafe.admin.server.business.request.base.AddFireFighterRequest;
import com.vsafe.admin.server.business.request.base.UpdateFireFighterRequest;
import com.vsafe.admin.server.business.request.base.SearchFireFighterRequest;
import com.vsafe.admin.server.business.response.BaseResponse;

public interface FireFighterService {

    BaseResponse searchWithPaging(SearchFireFighterRequest request);

    BaseResponse addFireFighter(AddFireFighterRequest request);

    BaseResponse updateFireFighter(UpdateFireFighterRequest request);

    BaseResponse doUpdateStatus();

    BaseResponse getById(String id);

}