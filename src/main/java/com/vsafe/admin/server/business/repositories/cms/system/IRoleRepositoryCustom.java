package com.vsafe.admin.server.business.repositories.cms.system;

import com.vsafe.admin.server.business.entities.system.RoleEntity;
import com.vsafe.admin.server.business.repositories.BaseRepository;
import com.vsafe.admin.server.business.request.system.SearchRoleRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface IRoleRepositoryCustom {
    Page<RoleEntity> searchRole(SearchRoleRequest request, String clientId);
}
