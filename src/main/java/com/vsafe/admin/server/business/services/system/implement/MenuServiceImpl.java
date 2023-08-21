package com.vsafe.admin.server.business.services.system.implement;

import com.vsafe.admin.server.business.entities.system.EmployeeRoleEntity;
import com.vsafe.admin.server.business.entities.system.MenuEntity;
import com.vsafe.admin.server.business.entities.system.RoleEntity;
import com.vsafe.admin.server.business.repositories.cms.system.IEmployeeRoleRepositoryJpa;
import com.vsafe.admin.server.business.repositories.cms.system.IMenuRepositoryCustom;
import com.vsafe.admin.server.business.repositories.cms.system.IMenuRepositoryJpa;
import com.vsafe.admin.server.business.repositories.cms.system.IRoleRepositoryJpa;
import com.vsafe.admin.server.business.request.system.PermissionRoleRequest;
import com.vsafe.admin.server.business.request.system.SearchMenuRequest;
import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.business.response.system.UserDetailResponse;
import com.vsafe.admin.server.business.services.common.UserDetailService;
import com.vsafe.admin.server.business.services.system.MenuService;
import com.vsafe.admin.server.helpers.enums.UserType;
import com.vsafe.admin.server.helpers.enums.responseStatus.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.vsafe.admin.server.helpers.constants.BusinessConstant.Status.ACTIVE;

@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    private final IMenuRepositoryCustom iMenuRepositoryCustom;
    private final IMenuRepositoryJpa iMenuRepositoryJpa;
    private final UserDetailService userDetailService;
    private final IEmployeeRoleRepositoryJpa iEmployeeRoleRepositoryJpa;
    private final IRoleRepositoryJpa iRoleRepositoryJpa;

    public MenuServiceImpl(IMenuRepositoryCustom iMenuRepositoryCustom, IMenuRepositoryJpa iMenuRepositoryJpa, UserDetailService userDetailService, IEmployeeRoleRepositoryJpa iUserRoleRepositoryJpa, IRoleRepositoryJpa iRoleRepositoryJpa) {
        this.iMenuRepositoryCustom = iMenuRepositoryCustom;
        this.iMenuRepositoryJpa = iMenuRepositoryJpa;
        this.userDetailService = userDetailService;
        this.iEmployeeRoleRepositoryJpa = iUserRoleRepositoryJpa;
        this.iRoleRepositoryJpa = iRoleRepositoryJpa;
    }

    @Override
    public BaseResponse searchWithoutPaging(SearchMenuRequest request) {
        return BaseResponse.success(iMenuRepositoryCustom.searchWithoutPaging(request));
    }

    @Override
    public BaseResponse searchWithoutPaging(String request) {
        return BaseResponse.success(iMenuRepositoryCustom.searchWithoutPaging(request));
    }

    @Override
    public BaseResponse setPermissionToRole(PermissionRoleRequest request) {
        UserDetailResponse currentUser = userDetailService.getCurrentUser();
        if (currentUser == null) {
            return BaseResponse.error("Không có quyền thực hiện thao tác");
        }
        String clientId = Objects.equals(currentUser.getUserType(), UserType.CUSTOMER.getValue()) ? currentUser.getCustomerInfo().getClientId() : currentUser.getEmployeeInfo().getClientId();
        MenuEntity currentMenu = iMenuRepositoryJpa.findFirstByIdAndClientId(request.getMenuId(), clientId);
        if (currentMenu == null) {
            return BaseResponse.error(ErrorCode.BusinessErrorCode.BUS404);
        }
        currentMenu.setPermission(request.getPermission());
        iMenuRepositoryJpa.save(currentMenu);
        return BaseResponse.success(currentMenu);
    }

    @Override
    public BaseResponse getRoleOfMenu(String menuId) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        MenuEntity currentMenu = iMenuRepositoryJpa.findFirstByIdAndClientId(menuId, clientId);
        if (currentMenu == null) {
            return BaseResponse.error(ErrorCode.BusinessErrorCode.BUS404);
        }
        return BaseResponse.success(currentMenu.getPermission());
    }

    @Override
    public BaseResponse getMenuByUserLogin() {
        List<MenuEntity> menuByPermission = new ArrayList<>();
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        List<EmployeeRoleEntity> employeeRoleEntity = iEmployeeRoleRepositoryJpa.findByEmployeeId(Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getId() : userDetailResponse.getCustomerInfo().getId());
        List<RoleEntity> roleListActive = iRoleRepositoryJpa.findByStatus(ACTIVE);
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        List<String> roleIds = new ArrayList<>();
        Set<String> pathLevelMenu = new HashSet<>();
        if (employeeRoleEntity != null && employeeRoleEntity.size() > 0 && roleListActive.size() > 0) {
            for (EmployeeRoleEntity roleItem : employeeRoleEntity) {
                for (RoleEntity roleItemActive : roleListActive) {
                    if (roleItem.getRoleId().equals(roleItemActive.getId()))
                        roleIds.add(roleItem.getRoleId());
                }
            }
            if (roleIds.size() > 0) {
                List<MenuEntity> menuListByRoles = iMenuRepositoryCustom.findByRoleIdInAndClientIdAndStatus(roleIds, clientId, List.of(ACTIVE));
                if (menuListByRoles != null && menuListByRoles.size() > 0) {
                    for (MenuEntity menuItem : menuListByRoles) {
                        if (!StringUtils.isEmpty(menuItem.getPathId())) {
                            pathLevelMenu.addAll(Arrays.asList(menuItem.getPathId().split(",")));
                        }
                    }
                }
                menuByPermission = iMenuRepositoryCustom.findByIdInAndClientId(pathLevelMenu, clientId);
            }
        }
        return BaseResponse.success(menuByPermission);
    }

    @Override
    public BaseResponse updateMenu(MenuEntity request) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        MenuEntity currentMenu = iMenuRepositoryJpa.findFirstByIdAndClientId(request.getId(), clientId);
        if (currentMenu != null) {
            currentMenu.setName(request.getName());
            currentMenu.setStatus(request.getStatus());
            currentMenu.setDescription(request.getDescription());
            currentMenu.setIcon(request.getIcon());
            currentMenu.setUrl(request.getUrl());
            currentMenu.setParentId(request.getParentId());
            iMenuRepositoryJpa.save(currentMenu);
            return BaseResponse.success(currentMenu);
        }
        return BaseResponse.error("Thông tin Menu không tồn tại");
    }

    @Override
    public BaseResponse updateStateMenu(MenuEntity request) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        MenuEntity currentMenu = iMenuRepositoryJpa.findFirstByIdAndClientId(request.getId(), clientId);
        if (currentMenu != null) {
            currentMenu.setStatus(currentMenu.getStatus() == 1 ? 0 : 1);
            iMenuRepositoryJpa.save(currentMenu);
            return BaseResponse.success(currentMenu);
        }
        return BaseResponse.error("Thông tin Menu không tồn tại");
    }
}