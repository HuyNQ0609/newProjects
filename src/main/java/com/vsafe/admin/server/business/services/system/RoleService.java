package com.vsafe.admin.server.business.services.system;

import com.vsafe.admin.server.business.request.system.SearchRoleRequest;
import com.vsafe.admin.server.business.response.BaseResponse;

import java.util.List;

public interface RoleService {

    BaseResponse getAllActive();

    BaseResponse searchRole(SearchRoleRequest request);

    BaseResponse addRole(SearchRoleRequest request);

    BaseResponse updateRole(SearchRoleRequest request);

    BaseResponse updateStatusRole(SearchRoleRequest request);

    BaseResponse removeUserRole(List<String> userIds, String roleId);
}