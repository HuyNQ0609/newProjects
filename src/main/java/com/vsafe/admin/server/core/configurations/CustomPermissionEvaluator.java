package com.vsafe.admin.server.core.configurations;

import com.vsafe.admin.server.business.entities.system.MenuEntity;
import com.vsafe.admin.server.business.entities.system.RoleEntity;
import com.vsafe.admin.server.business.entities.system.EmployeeRoleEntity;
import com.vsafe.admin.server.business.entities.operation.sub.MenuPermission;
import com.vsafe.admin.server.business.repositories.cms.system.IMenuRepositoryJpa;
import com.vsafe.admin.server.business.repositories.cms.system.IRoleRepositoryJpa;
import com.vsafe.admin.server.business.repositories.cms.system.IEmployeeRoleRepositoryJpa;
import com.vsafe.admin.server.business.response.system.UserDetailResponse;
import com.vsafe.admin.server.business.services.common.UserDetailService;
import com.vsafe.admin.server.helpers.constants.SystemConstants;
import com.vsafe.admin.server.helpers.enums.UserType;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.vsafe.admin.server.helpers.constants.BusinessConstant.Status.ACTIVE;

@Service
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final IEmployeeRoleRepositoryJpa iEmployeeRoleRepositoryJpa;
    private final IMenuRepositoryJpa iMenuRepositoryJpa;

    private final UserDetailService userDetailService;

    private final IRoleRepositoryJpa iRoleRepositoryJpa;

    public CustomPermissionEvaluator(IEmployeeRoleRepositoryJpa iUserRoleRepositoryJpa, IMenuRepositoryJpa iMenuRepositoryJpa, UserDetailService userDetailService, IRoleRepositoryJpa iRoleRepositoryJpa) {
        this.iEmployeeRoleRepositoryJpa = iUserRoleRepositoryJpa;
        this.iMenuRepositoryJpa = iMenuRepositoryJpa;
        this.userDetailService = userDetailService;
        this.iRoleRepositoryJpa = iRoleRepositoryJpa;
    }

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(targetDomainObject.toString().toUpperCase(), permission.toString().toUpperCase());
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(targetType.toUpperCase(), permission.toString().toUpperCase());
    }

    private boolean hasPrivilege(String targetType, String permission) {
        if (!SystemConstants.ACTION_FEATURE.contains(permission))
            return false;
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        List<EmployeeRoleEntity> employeeRoleEntity = iEmployeeRoleRepositoryJpa.findByEmployeeId(Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getId() : userDetailResponse.getCustomerInfo().getId());
        List<RoleEntity> roleListActive = iRoleRepositoryJpa.findByStatus(ACTIVE);
        List<String> roleIds = new ArrayList<>();
        if (employeeRoleEntity != null && employeeRoleEntity.size() > 0 && roleListActive.size() > 0) {
            for (EmployeeRoleEntity roleItem : employeeRoleEntity) {
                for (RoleEntity roleItemActive : roleListActive) {
                    if (roleItem.getRoleId().equals(roleItemActive.getId()))
                        roleIds.add(roleItem.getRoleId());
                }
            }
            MenuEntity menuEntity = iMenuRepositoryJpa.findFirstByCodeAndClientId(targetType, clientId);
            if (menuEntity != null && menuEntity.getPermission() != null && menuEntity.getPermission().size() > 0) {
                for (MenuPermission menuPermission : menuEntity.getPermission()) {
                    for (String roleId : roleIds) {
                        if (Objects.equals(menuPermission.getRoleId(), roleId) && menuPermission.getActions().contains(permission))
                            return true;
                    }
                }
            }
        }
        return false;
    }

}
