package com.vsafe.admin.server.business.repositories.cms.operation;

import com.vsafe.admin.server.business.entities.operation.DeviceConfigEntity;
import com.vsafe.admin.server.business.repositories.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface IDeviceRepositoryJpa extends BaseRepository<DeviceConfigEntity, String> {

    List<DeviceConfigEntity> findByCustomerIdAndIsDeletedAndClientId(String customerId, Integer isDeleted, String clientId);
    List<DeviceConfigEntity> findByLocationIdAndIsDeletedAndClientId(String locationId, Integer isDeleted, String clientId);
    List<DeviceConfigEntity> findByLocationIdAndClientId(String locationId, String clientId);
}
