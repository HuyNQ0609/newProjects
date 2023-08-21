package com.vsafe.admin.server.business.response.base;

import com.vsafe.admin.server.business.entities.base.FireFighterEntity;
import com.vsafe.admin.server.business.entities.base.Representative;
import com.vsafe.admin.server.helpers.utils.mapper.MapperFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FireFighterResponse {

    private String id;

    private String orgName;

    private String hotline;

    private String hotlineBackup;

    private String address;

    private Long provinceId;

    private Long districtId;

    private Representative representative;

    private Representative representativeBackup;

    public static FireFighterResponse of(FireFighterEntity fireFighterEntity) {
        if (Objects.isNull(fireFighterEntity)) return null;
        ModelMapper mapper = MapperFactory.mapper(MapperFactory.ModelMapperOptions.DEEP_COPY);
        return mapper.map(fireFighterEntity, FireFighterResponse.class);
    }

    public static List<FireFighterResponse> of(List<FireFighterEntity> fireFighterEntities) {
        if (CollectionUtils.isEmpty(fireFighterEntities)) return new ArrayList<>();
        ModelMapper mapper = MapperFactory.mapper(MapperFactory.ModelMapperOptions.DEEP_COPY);
        return fireFighterEntities.stream().map(fireFighterEntity -> mapper.map(fireFighterEntity, FireFighterResponse.class)).collect(Collectors.toList());
    }

}
