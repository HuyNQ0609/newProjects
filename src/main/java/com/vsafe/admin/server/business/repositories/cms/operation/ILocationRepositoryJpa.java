package com.vsafe.admin.server.business.repositories.cms.operation;

import com.vsafe.admin.server.business.entities.operation.DeviceConfigEntity;
import com.vsafe.admin.server.business.entities.operation.LocationConfigEntity;
import com.vsafe.admin.server.business.repositories.BaseRepository;

import java.util.List;

public interface ILocationRepositoryJpa extends BaseRepository<LocationConfigEntity, String> {

    List<LocationConfigEntity> findByCustomerIdAndIsDeletedAndClientId(String customerId, Integer isDeleted, String clientId);

    List<LocationConfigEntity> findByAreaIdAndClientIdOrderByCreatedDateDesc(String areaId, String clientId);

    LocationConfigEntity findFirstById(String id);
}
