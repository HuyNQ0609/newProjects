package com.vsafe.admin.server.business.services.system.implement;

import com.vsafe.admin.server.business.entities.operation.CustomerEntity;
import com.vsafe.admin.server.business.entities.system.SystemConfigEntity;
import com.vsafe.admin.server.business.repositories.cms.system.ISystemConfigRepositoryCustom;
import com.vsafe.admin.server.business.repositories.cms.system.ISystemConfigRepositoryJpa;
import com.vsafe.admin.server.business.request.system.SearchSystemConfigRequest;
import com.vsafe.admin.server.business.request.system.SystemConfigSaveRequest;
import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.business.response.system.UserDetailResponse;
import com.vsafe.admin.server.business.services.common.UserDetailService;
import com.vsafe.admin.server.business.services.system.SystemConfigService;
import com.vsafe.admin.server.helpers.enums.UserType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class SystemConfigServiceImpl implements SystemConfigService {
    private final UserDetailService userDetailService;
    private final ISystemConfigRepositoryJpa iSystemConfigRepositoryJpa;
    private final ISystemConfigRepositoryCustom iSystemConfigRepositoryCustom;

    public SystemConfigServiceImpl(UserDetailService userDetailService, ISystemConfigRepositoryJpa iSystemConfigRepositoryJpa,
                                   ISystemConfigRepositoryCustom iSystemConfigRepositoryCustom) {
        this.userDetailService = userDetailService;
        this.iSystemConfigRepositoryJpa = iSystemConfigRepositoryJpa;
        this.iSystemConfigRepositoryCustom = iSystemConfigRepositoryCustom;
    }

    @Override
    public BaseResponse searchWithPaging(SearchSystemConfigRequest request) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        List<SystemConfigEntity> systemConfigEntities = iSystemConfigRepositoryCustom.searchWithPaging(request, clientId);
        long total = iSystemConfigRepositoryCustom.countSearch(request, clientId);
        Map<String, Long> dataTotal = new HashMap<>();
        dataTotal.put("total", total);
        return BaseResponse.success("Lấy dữ liệu thành công", systemConfigEntities, dataTotal);
    }

    @Override
    public BaseResponse saveSysConfig(SystemConfigSaveRequest request) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue())
                ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();

        String id = request.getId();
        String code = request.getCode();
        SystemConfigEntity systemConfigEntitySave = null;

        if (StringUtils.isNotEmpty(id)) {
            systemConfigEntitySave = iSystemConfigRepositoryJpa.findByIdAndClientId(id, clientId);
            if (Objects.isNull(systemConfigEntitySave)) {
                return BaseResponse.error("Không tồn tại bản ghi cấu hình trên hoặc bạn không có quyền chỉnh sửa");
            }
        }

        SystemConfigEntity configEntityCheck = iSystemConfigRepositoryJpa.findByCodeAndClientId(code, clientId);
        if (Objects.nonNull(configEntityCheck)) {
            if (StringUtils.isEmpty(id) || !id.equals(configEntityCheck.getId())) {
                return BaseResponse.error("Mã code đã tồn tại");
            }
        }

        if (Objects.isNull(systemConfigEntitySave)) {
            systemConfigEntitySave = new SystemConfigEntity();
        }
        systemConfigEntitySave.setCode(code);
        systemConfigEntitySave.setValue(request.getValue());
        systemConfigEntitySave.setDescription(request.getDescription());
        systemConfigEntitySave.setStatus(request.getStatus());
        systemConfigEntitySave.setClientId(clientId);

        iSystemConfigRepositoryJpa.save(systemConfigEntitySave);

        return BaseResponse.success(Objects.nonNull(id) ? "Chỉnh sửa cấu hình thành công" : "Thêm mới cấu hình thành công", systemConfigEntitySave, null);
    }
}
