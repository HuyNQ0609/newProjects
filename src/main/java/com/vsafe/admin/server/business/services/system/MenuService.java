package com.vsafe.admin.server.business.services.system;

import com.vsafe.admin.server.business.entities.system.MenuEntity;
import com.vsafe.admin.server.business.request.system.PermissionRoleRequest;
import com.vsafe.admin.server.business.request.system.SearchMenuRequest;
import com.vsafe.admin.server.business.response.BaseResponse;

public interface MenuService {

    BaseResponse searchWithoutPaging(SearchMenuRequest request);

    BaseResponse searchWithoutPaging(String request);

    BaseResponse setPermissionToRole(PermissionRoleRequest request);

    BaseResponse getRoleOfMenu(String menuId);

    BaseResponse getMenuByUserLogin();

    BaseResponse updateMenu(MenuEntity request);

    BaseResponse updateStateMenu(MenuEntity request);
}