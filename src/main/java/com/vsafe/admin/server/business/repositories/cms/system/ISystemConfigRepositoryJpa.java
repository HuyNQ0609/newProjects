package com.vsafe.admin.server.business.repositories.cms.system;

import com.vsafe.admin.server.business.entities.system.SystemConfigEntity;
import com.vsafe.admin.server.business.repositories.BaseRepository;

public interface ISystemConfigRepositoryJpa extends BaseRepository<SystemConfigEntity, String> {
    boolean existsByCodeAndClientId(String code, String clientId);

    SystemConfigEntity findByCodeAndClientId(String code, String clientId);

    SystemConfigEntity findByIdAndClientId(String id, String clientId);
}
