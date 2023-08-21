package com.vsafe.admin.server.business.services.system;

import com.vsafe.admin.server.business.request.system.SearchSystemConfigRequest;
import com.vsafe.admin.server.business.request.system.SystemConfigSaveRequest;
import com.vsafe.admin.server.business.response.BaseResponse;

public interface SystemConfigService {
    BaseResponse searchWithPaging(SearchSystemConfigRequest request);

    BaseResponse saveSysConfig(SystemConfigSaveRequest request);
}
