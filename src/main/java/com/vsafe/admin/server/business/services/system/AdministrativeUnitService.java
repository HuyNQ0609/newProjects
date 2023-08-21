package com.vsafe.admin.server.business.services.system;

import com.vsafe.admin.server.business.request.system.AdministrativeUnitFilter;
import com.vsafe.admin.server.business.request.system.AdministrativeUnitRequest;
import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.business.response.system.AdministrativeUnitResponse;
import com.vsafe.admin.server.business.response.system.FlatAdministrativeUnitResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.vsafe.admin.server.core.exceptions.SystemException;

public interface AdministrativeUnitService {
    void insertData(MultipartFile file);

    Page<FlatAdministrativeUnitResponse> listAll(AdministrativeUnitFilter filter);
    Page<AdministrativeUnitResponse> list(AdministrativeUnitFilter filter);

    AdministrativeUnitResponse save(AdministrativeUnitRequest request) throws SystemException;

    AdministrativeUnitResponse detail(String code);

    BaseResponse getListByTypeAndParent(Integer type, String parentCode);
}
