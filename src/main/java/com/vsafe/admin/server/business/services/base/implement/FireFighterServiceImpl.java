package com.vsafe.admin.server.business.services.base.implement;

import com.vsafe.admin.server.business.entities.base.FireFighterEntity;
import com.vsafe.admin.server.business.repositories.cms.base.IFireFighterRepositoryCustom;
import com.vsafe.admin.server.business.repositories.cms.base.IFireFighterRepositoryJpa;
import com.vsafe.admin.server.business.request.base.AddFireFighterRequest;
import com.vsafe.admin.server.business.request.base.SearchFireFighterRequest;
import com.vsafe.admin.server.business.request.base.UpdateFireFighterRequest;
import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.business.response.base.FireFighterResponse;
import com.vsafe.admin.server.business.services.base.FireFighterService;
import com.vsafe.admin.server.helpers.constants.BusinessConstant;
import com.vsafe.admin.server.helpers.enums.responseStatus.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FireFighterServiceImpl implements FireFighterService {

    private final IFireFighterRepositoryCustom fireFighterRepositoryCustom;
    private final IFireFighterRepositoryJpa fireFighterRepositoryJpa;

    public FireFighterServiceImpl(IFireFighterRepositoryCustom fireFighterRepositoryCustom, IFireFighterRepositoryJpa fireFighterRepositoryJpa) {
        this.fireFighterRepositoryCustom = fireFighterRepositoryCustom;
        this.fireFighterRepositoryJpa = fireFighterRepositoryJpa;
    }

    @Override
    public BaseResponse searchWithPaging(SearchFireFighterRequest request) {
        if (request.getLevel() == null)
            request.setLevel(BusinessConstant.FireFighterLevel.PROVINCE);
        return BaseResponse.success("Lấy dữ liệu thành công", fireFighterRepositoryCustom.searchWithPaging(request), fireFighterRepositoryCustom.countSearch(request));
    }

    @Override
    public BaseResponse addFireFighter(AddFireFighterRequest request) {



        return null;
    }

    @Override
    public BaseResponse updateFireFighter(UpdateFireFighterRequest request) {
        Optional<FireFighterEntity> currentEntityOptional = fireFighterRepositoryJpa.findById(request.getId());
        if (currentEntityOptional.isEmpty()) {
            return BaseResponse.error(ErrorCode.BusinessErrorCode.BUS404);
        }
        FireFighterEntity currentEntity = currentEntityOptional.get();
        currentEntity.setAddress(StringUtils.isEmpty(request.getAddress()) ? currentEntity.getAddress() : request.getAddress());
        currentEntity.setHotline(StringUtils.isEmpty(request.getHotline()) ? currentEntity.getHotline() : request.getHotline());
        currentEntity.setHotlineBackup(StringUtils.isEmpty(request.getHotlineBackup()) ? currentEntity.getHotlineBackup() : request.getHotlineBackup());
        currentEntity.setOrgName(StringUtils.isEmpty(request.getOrgName()) ? currentEntity.getOrgName() : request.getOrgName());
        currentEntity.setProvinceId(request.getProvinceId() == null ? currentEntity.getProvinceId() : request.getProvinceId());
        currentEntity.setDistrictId(request.getDistrictId() == null ? currentEntity.getDistrictId() : request.getDistrictId());
        if (request.getRepresentative() != null) {
            currentEntity.setRepresentative(request.getRepresentative());
        }
        if (request.getRepresentativeBackup() != null) {
            currentEntity.setRepresentative(request.getRepresentativeBackup());
        }
        fireFighterRepositoryJpa.save(currentEntity);
        return BaseResponse.success(currentEntity);
    }

    @Override
    public BaseResponse doUpdateStatus() {
        return null;
    }

    @Override
    public BaseResponse getById(String id) {
        FireFighterEntity fireFighterEntity = fireFighterRepositoryJpa.findById(id).orElse(null);
        return BaseResponse.success(FireFighterResponse.of(fireFighterEntity));
    }
}
