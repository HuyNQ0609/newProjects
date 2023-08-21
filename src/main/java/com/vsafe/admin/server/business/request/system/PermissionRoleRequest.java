package com.vsafe.admin.server.business.request.system;

import com.vsafe.admin.server.business.entities.operation.sub.MenuPermission;
import com.vsafe.admin.server.business.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PermissionRoleRequest extends BaseRequest {

    @NotNull(message = "ID chức năng không được để trống")
    private String menuId;

    private List<MenuPermission> permission;
}
