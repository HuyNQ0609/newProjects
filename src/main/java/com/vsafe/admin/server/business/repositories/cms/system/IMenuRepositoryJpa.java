package com.vsafe.admin.server.business.repositories.cms.system;

import com.vsafe.admin.server.business.entities.system.MenuEntity;
import com.vsafe.admin.server.business.repositories.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IMenuRepositoryJpa extends BaseRepository<MenuEntity, String> {
    MenuEntity findFirstByCodeAndClientId(String code, String clientId);

    List<MenuEntity> findByIdInAndClientId(Set<String> id, String clientId);
}
