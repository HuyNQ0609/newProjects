package com.vsafe.admin.server.business.request.system;

import com.vsafe.admin.server.business.request.BaseRequest;
import lombok.Data;

import java.util.List;

@Data
public class UserByRoleRequest extends BaseRequest {
    private List<String> userIds;
    private String roleId;
}
