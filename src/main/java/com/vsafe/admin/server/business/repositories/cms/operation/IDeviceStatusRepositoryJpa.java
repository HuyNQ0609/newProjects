package com.vsafe.admin.server.business.repositories.cms.operation;

import com.vsafe.admin.server.business.entities.operation.DeviceStatusEntity;
import com.vsafe.admin.server.business.repositories.BaseRepository;
import org.springframework.data.mongodb.repository.Aggregation;

import java.util.List;

public interface IDeviceStatusRepositoryJpa extends BaseRepository<DeviceStatusEntity, String> {
    @Aggregation(pipeline = {
            "{'$match':{'status':?0}}",
            "{'$setWindowFields': {partitionBy: '$device_id', sortBy: { 'created_date': -1 }, output: {'rank': {'$rank': {}}} }}",
            "{'$match':{'rank': 1}}"
    })
    List<DeviceStatusEntity> getListOrderTopRank(Integer status);
}
