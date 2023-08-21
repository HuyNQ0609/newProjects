package com.vsafe.admin.server.business.repositories.cms.system;

import com.vsafe.admin.server.business.entities.system.AdministrativeUnitEntity;
import com.vsafe.admin.server.business.response.system.FlatAdministrativeUnitResponse;

import java.util.List;

public interface IAdministrativeUnitRepositoryCustom {
    List<AdministrativeUnitEntity> list(String name);

    List<FlatAdministrativeUnitResponse> flatList(String name);

    List<AdministrativeUnitEntity> getListByTypeAndParentCode(Integer type, String parentCode);
}
