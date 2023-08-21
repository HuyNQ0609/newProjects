package com.vsafe.admin.server.business.repositories.cms.system;

import com.vsafe.admin.server.business.entities.system.RoleEntity;
import com.vsafe.admin.server.business.repositories.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRoleRepositoryJpa extends BaseRepository<RoleEntity, String> {

    List<RoleEntity> findByStatus(Integer status);
}
