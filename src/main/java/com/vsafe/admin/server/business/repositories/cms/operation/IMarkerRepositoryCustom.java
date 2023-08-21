package com.vsafe.admin.server.business.repositories.cms.operation;

import com.vsafe.admin.server.business.entities.operation.AreaEntity;
import com.vsafe.admin.server.business.request.operation.SearchMarkerRequest;

import java.util.List;

public interface IMarkerRepositoryCustom {
    List<AreaEntity> searchWithoutPaging(SearchMarkerRequest markerRequest, String clientId);
}
