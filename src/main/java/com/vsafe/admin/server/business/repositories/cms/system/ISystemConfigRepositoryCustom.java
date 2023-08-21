package com.vsafe.admin.server.business.repositories.cms.system;

import com.vsafe.admin.server.business.entities.system.SystemConfigEntity;
import com.vsafe.admin.server.business.request.system.SearchSystemConfigRequest;

import java.util.List;

public interface ISystemConfigRepositoryCustom {

    long countSearch(SearchSystemConfigRequest request, String clientId);

    List<SystemConfigEntity> searchWithPaging(SearchSystemConfigRequest request, String clientId);
}
