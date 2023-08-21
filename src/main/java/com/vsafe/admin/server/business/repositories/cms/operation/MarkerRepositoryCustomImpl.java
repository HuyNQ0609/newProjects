package com.vsafe.admin.server.business.repositories.cms.operation;

import com.vsafe.admin.server.business.entities.operation.AreaEntity;
import com.vsafe.admin.server.business.request.operation.SearchMarkerRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MarkerRepositoryCustomImpl implements IMarkerRepositoryCustom {
    private final IMarkerRepositoryJpa iMarkerRepositoryJpa;
    public MarkerRepositoryCustomImpl(IMarkerRepositoryJpa iMarkerRepositoryJpa, MongoTemplate mongotemplate) {
        this.iMarkerRepositoryJpa = iMarkerRepositoryJpa;
    }

    @Override
    public List<AreaEntity> searchWithoutPaging(SearchMarkerRequest markerRequest, String clientId) {
        String provinceCode = markerRequest.getProvinceCode();
        String districtCode = markerRequest.getDistrictCode();
        String wardCode = markerRequest.getWardCode();
        List<AreaEntity> markerEntities = null;
        int numNotNull = 1;

        if (StringUtils.isNotEmpty(provinceCode)) {
            numNotNull += 1;
        }
        if (StringUtils.isNotEmpty(provinceCode) && StringUtils.isNotEmpty(districtCode)) {
            numNotNull += 1;
        }
        if (StringUtils.isNotEmpty(provinceCode) && StringUtils.isNotEmpty(districtCode) && StringUtils.isNotEmpty(wardCode)) {
            numNotNull += 1;
        }
        switch (numNotNull) {
            case 1:
                markerEntities = iMarkerRepositoryJpa.findByClientId(clientId);
                break;
            case 2:
                markerEntities = iMarkerRepositoryJpa.findByProvinceCodeAndClientId(provinceCode, clientId);
                break;
            case 3:
                markerEntities = iMarkerRepositoryJpa.findByProvinceCodeAndDistrictCodeAndClientId(provinceCode, districtCode, clientId);
                break;
            case 4:
                markerEntities = iMarkerRepositoryJpa.findByProvinceCodeAndDistrictCodeAndWardCodeAndClientId(provinceCode, districtCode, wardCode, clientId);
                break;
            default:
                break;
        }
        return markerEntities;
    }
}
