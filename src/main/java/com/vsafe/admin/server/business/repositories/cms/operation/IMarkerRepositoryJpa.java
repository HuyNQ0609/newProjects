package com.vsafe.admin.server.business.repositories.cms.operation;

import com.vsafe.admin.server.business.entities.operation.AreaEntity;
import com.vsafe.admin.server.business.entities.operation.DeviceStatusEntity;
import com.vsafe.admin.server.business.repositories.BaseRepository;
import org.springframework.data.mongodb.repository.Aggregation;

import java.util.List;
import java.util.Optional;

public interface IMarkerRepositoryJpa extends BaseRepository<AreaEntity, String> {
    Optional<AreaEntity> findById(String id);

    List<AreaEntity> findByClientId(String clientId);

    List<AreaEntity> findByProvinceCodeAndClientId(String provinceCode, String clientId);

    List<AreaEntity> findByProvinceCodeAndDistrictCodeAndClientId(String provinceCode, String districtCode, String clientId);

    List<AreaEntity> findByProvinceCodeAndDistrictCodeAndWardCodeAndClientId(String provinceCode, String districtCode, String wardCode, String clientId);

    List<AreaEntity> findAllByClientId(String clientId);

    @Aggregation(pipeline = {
            "{'$match':{'clientId':?0}}",
            "{'$setWindowFields': {partitionBy: '$province_code', sortBy: { 'created_date': -1 }, output: {'rank': {'$rank': {}}} }}",
            "{'$match':{'rank': 1}}"
    })
    List<AreaEntity> getGroupArea(String clientId);
}
