package com.vsafe.admin.server.business.services.system;

import com.vsafe.admin.server.business.entities.system.CatalogEntity;
import com.vsafe.admin.server.business.entities.system.CatalogTypeEntity;
import com.vsafe.admin.server.business.request.system.CatalogFilter;
import com.vsafe.admin.server.business.request.system.CatalogRequest;
import com.vsafe.admin.server.business.request.system.CatalogTypeRequest;
import com.vsafe.admin.server.business.response.system.CatalogResponse;
import com.vsafe.admin.server.business.response.system.CatalogTypeResponse;
import com.vsafe.admin.server.core.exceptions.SystemException;

import java.util.List;

public interface CatalogService {
    CatalogTypeEntity save(CatalogTypeRequest request) throws SystemException;
    CatalogEntity save(CatalogRequest request) throws SystemException;

    List<CatalogTypeResponse> getCatalogTypeALl();

    List<CatalogResponse> getCatalogList(CatalogFilter filter);

    void deleteCatalogType(String id);

    CatalogTypeEntity changeCatalogTypeState(String catalogTypeId);
    CatalogEntity changeCatalogState(String catalogTypeId);
}
