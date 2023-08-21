package com.vsafe.admin.server.business.repositories.cms.system;

import com.vsafe.admin.server.business.entities.system.MenuEntity;
import com.vsafe.admin.server.business.request.system.SearchMenuRequest;

import java.util.List;
import java.util.Set;

public interface IMenuRepositoryCustom {
    List<MenuEntity> searchWithoutPaging(SearchMenuRequest request);

    List<MenuEntity> searchWithoutPaging(String request);

    List<MenuEntity> findByRoleIdInAndClientIdAndStatus(List<String> roleIds, String clientId, List<Integer> status);

    List<MenuEntity> findByIdInAndClientId(Set<String> id, String clientId);
}
