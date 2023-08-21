package com.vsafe.admin.server.business.request.base;

import com.vsafe.admin.server.business.request.BaseRequest;
import com.vsafe.admin.server.helpers.constants.SystemConstants;
import lombok.Data;

@Data
public class SearchFireFighterRequest extends BaseRequest {
    private String orgName;
    private Long provinceId;
    private Long districtId;
    private Long level;
    private String parentId;
    private int pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
    private int pageNumber = SystemConstants.DEFAULT_PAGE_NUMBER;
}
