package com.vsafe.admin.server.business.services.opearation.implement;

import com.vsafe.admin.server.business.entities.operation.DeviceConfigEntity;
import com.vsafe.admin.server.business.entities.operation.LocationConfigEntity;
import com.vsafe.admin.server.business.entities.operation.sub.SensorEntity;
import com.vsafe.admin.server.business.repositories.cms.operation.IDeviceRepositoryJpa;
import com.vsafe.admin.server.business.repositories.cms.operation.ILocationRepositoryJpa;
import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.business.response.operation.SensorResponse;
import com.vsafe.admin.server.business.response.system.UserDetailResponse;
import com.vsafe.admin.server.business.services.common.UserDetailService;
import com.vsafe.admin.server.business.services.opearation.DeviceService;
import com.vsafe.admin.server.helpers.constants.BusinessConstant;
import com.vsafe.admin.server.helpers.enums.UserType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final IDeviceRepositoryJpa iDeviceRepositoryJpa;
    private final UserDetailService userDetailService;

    private final ILocationRepositoryJpa iLocationRepositoryJpa;

    public DeviceServiceImpl(IDeviceRepositoryJpa iDeviceRepositoryJpa, UserDetailService userDetailService, ILocationRepositoryJpa iLocationRepositoryJpa) {
        this.iDeviceRepositoryJpa = iDeviceRepositoryJpa;
        this.userDetailService = userDetailService;
        this.iLocationRepositoryJpa = iLocationRepositoryJpa;
    }

    @Override
    public BaseResponse searchByCustomer(String customerId) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();

        List<DeviceConfigEntity> deviceConfigEntities = iDeviceRepositoryJpa.findByCustomerIdAndIsDeletedAndClientId(customerId, BusinessConstant.DELETED_VALUE.NOT_DEL, clientId);
        List<SensorResponse> resultDevice = new ArrayList<>();
        SensorResponse resultItem;

        LocationConfigEntity locationConfigEntity;

        if (deviceConfigEntities != null && deviceConfigEntities.size() > 0) {
            for (DeviceConfigEntity itemDevice : deviceConfigEntities) {
                locationConfigEntity = iLocationRepositoryJpa.findFirstById(itemDevice.getLocationId());
                if (StringUtils.isNotEmpty(itemDevice.getGatewayImei())) {
                    resultItem = new SensorResponse();
                    resultItem.setImei(itemDevice.getGatewayImei());
                    resultItem.setName(itemDevice.getGatewayName());
                    resultItem.setStatus(itemDevice.getStatus());
                    resultItem.setType(BusinessConstant.DEVICE_TYPE.GATEWAY);
                    resultItem.setLocation(locationConfigEntity.getName());
                    resultItem.setArea(locationConfigEntity.getAreaName());
                    resultDevice.add(resultItem);
                }
                if (itemDevice.getSensorList() != null && itemDevice.getSensorList().size() > 0)
                    for (SensorEntity itemDeviceSensor : itemDevice.getSensorList()) {
                        resultItem = new SensorResponse();
                        resultItem.setImei(itemDeviceSensor.getImei());
                        resultItem.setName(itemDeviceSensor.getName());
                        resultItem.setStatus(itemDeviceSensor.getStatus());
                        resultItem.setType(itemDeviceSensor.getType());
                        resultItem.setLocation(locationConfigEntity.getName());
                        resultItem.setArea(locationConfigEntity.getAreaName());
                        resultDevice.add(resultItem);
                    }
            }
        }
        return BaseResponse.success("Danh sách thiết bị", resultDevice, resultDevice.size());
    }
}
