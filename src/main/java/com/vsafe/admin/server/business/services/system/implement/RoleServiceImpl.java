package com.vsafe.admin.server.business.services.system.implement;

import com.vsafe.admin.server.business.entities.system.EmployeeRoleEntity;
import com.vsafe.admin.server.business.entities.system.RoleEntity;
import com.vsafe.admin.server.business.repositories.cms.system.IEmployeeRepositoryJpa;
import com.vsafe.admin.server.business.repositories.cms.system.IEmployeeRoleRepositoryJpa;
import com.vsafe.admin.server.business.repositories.cms.system.IRoleRepositoryCustom;
import com.vsafe.admin.server.business.repositories.cms.system.IRoleRepositoryJpa;
import com.vsafe.admin.server.business.request.system.SearchRoleRequest;
import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.business.response.system.UserDetailResponse;
import com.vsafe.admin.server.business.services.common.UserDetailService;
import com.vsafe.admin.server.business.services.system.RoleService;
import com.vsafe.admin.server.helpers.enums.UserType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.vsafe.admin.server.helpers.constants.enums.subcriptions.ActiveStatusType.Constants.ACTIVE;

@Service
public class RoleServiceImpl implements RoleService {

    private final IRoleRepositoryJpa iRoleRepositoryJpa;
    private final IRoleRepositoryCustom iRoleRepositoryCustom;
    private final IEmployeeRoleRepositoryJpa iEmployeeRoleRepositoryJpa;
    private final IEmployeeRepositoryJpa iEmployeeRepositoryJpa;

    private final UserDetailService userDetailService;

    public RoleServiceImpl(IRoleRepositoryJpa iRoleRepositoryJpa, IRoleRepositoryCustom iRoleRepositoryCustom, IEmployeeRoleRepositoryJpa iUserRoleRepositoryJpa, IEmployeeRepositoryJpa iEmployeeRepositoryJpa, UserDetailService userDetailService) {
        this.iRoleRepositoryJpa = iRoleRepositoryJpa;
        this.iRoleRepositoryCustom = iRoleRepositoryCustom;
        this.iEmployeeRoleRepositoryJpa = iUserRoleRepositoryJpa;
        this.iEmployeeRepositoryJpa = iEmployeeRepositoryJpa;
        this.userDetailService = userDetailService;
    }

    @Override
    public BaseResponse getAllActive() {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        return BaseResponse.success(iRoleRepositoryJpa.findByClientIdAndStatus(clientId, ACTIVE));
    }

    @Override
    public BaseResponse searchRole(SearchRoleRequest request) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        Page<RoleEntity> roleEntityList = iRoleRepositoryCustom.searchRole(request, clientId);
        if (roleEntityList.getContent().size() > 0) {
            for (RoleEntity roleItem : roleEntityList.getContent()) {
                List<EmployeeRoleEntity> userRoleId = iEmployeeRoleRepositoryJpa.findByRoleId(roleItem.getId());
                List<String> employeeIds = userRoleId.stream().map(EmployeeRoleEntity::getEmployeeId).collect(Collectors.toList());
                roleItem.setEmployeeList(iEmployeeRepositoryJpa.findByIdIn(employeeIds));
            }
        }

        return BaseResponse.success("Thành công", roleEntityList.getContent(), roleEntityList.getTotalElements());
    }

    @Override
    public BaseResponse addRole(SearchRoleRequest request) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(request.getName());
        roleEntity.setDescription(request.getDescription());
        roleEntity.setStatus(ACTIVE);
        roleEntity.setClientId(clientId);
        roleEntity = iRoleRepositoryJpa.save(roleEntity);
        return BaseResponse.success(roleEntity);
    }

    @Override
    public BaseResponse updateRole(SearchRoleRequest request) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        RoleEntity roleEntity = iRoleRepositoryJpa.findFirstByIdAndClientId(request.getId(), clientId);

        if (roleEntity != null) {
            roleEntity.setName(request.getName());
            roleEntity.setDescription(request.getDescription());
            roleEntity = iRoleRepositoryJpa.save(roleEntity);
            return BaseResponse.success(roleEntity);
        }
        return BaseResponse.error("Không tồn tại thông tin Vai trò");
    }

    @Override
    public BaseResponse updateStatusRole(SearchRoleRequest request) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        RoleEntity roleEntity = iRoleRepositoryJpa.findFirstByIdAndClientId(request.getId(), clientId);

        if (roleEntity != null) {
            roleEntity.setStatus(request.getStatus());
            roleEntity = iRoleRepositoryJpa.save(roleEntity);
            return BaseResponse.success(roleEntity);
        }
        return BaseResponse.error("Không tồn tại thông tin Vai trò");
    }

    @Override
    public BaseResponse removeUserRole(List<String> userIds, String roleId) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        EmployeeRoleEntity employeeRoleEntity = iEmployeeRoleRepositoryJpa.findFirstByRoleIdAndEmployeeIdInAndClientId(roleId, userIds, clientId);
        if (employeeRoleEntity != null)
            iEmployeeRoleRepositoryJpa.delete(employeeRoleEntity);
        RoleEntity currentRole = iRoleRepositoryJpa.findFirstByIdAndClientId(roleId, clientId);
        return BaseResponse.success(currentRole);
    }
}
