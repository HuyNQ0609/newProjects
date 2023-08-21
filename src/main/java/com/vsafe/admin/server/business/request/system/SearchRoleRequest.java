package com.vsafe.admin.server.business.request.system;

import com.vsafe.admin.server.business.request.BaseRequest;
import lombok.Data;

@Data
public class SearchRoleRequest extends BaseRequest {
    private String id;
    private String name;
    private String description;
    private Integer status;
    private int page;
    private int pageSize;
}
