package com.vsafe.admin.server.business.request.operation;

import com.vsafe.admin.server.business.request.BaseRequest;
import com.vsafe.admin.server.helpers.constants.SystemConstants;
import lombok.Data;

@Data
public class SearchCustomerRequest extends BaseRequest {
    private String name;
    private Integer customerType;
    private Integer isExistsUser;
    private int pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
    private int pageNumber = SystemConstants.DEFAULT_PAGE_NUMBER;
}
