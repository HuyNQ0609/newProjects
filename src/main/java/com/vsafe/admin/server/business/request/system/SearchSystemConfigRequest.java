package com.vsafe.admin.server.business.request.system;

import com.vsafe.admin.server.business.request.BaseRequest;
import com.vsafe.admin.server.helpers.constants.SystemConstants;
import lombok.Data;

@Data
public class SearchSystemConfigRequest extends BaseRequest {
    private String title;

    private int pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
    private int pageNumber = SystemConstants.DEFAULT_PAGE_NUMBER;
}
