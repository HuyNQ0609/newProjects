package com.vsafe.admin.server.business.response.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vsafe.admin.server.business.entities.system.AdministrativeUnitEntity;
import com.vsafe.admin.server.helpers.utils.mapper.MapperFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdministrativeUnitResponse implements Serializable {
    private String code;

    private String name;

    private Integer type;

    private String path;

    private List<AdministrativeUnitResponse> parent;

    public static AdministrativeUnitResponse of (AdministrativeUnitEntity entity) {
        ModelMapper mapper = MapperFactory.mapper(MapperFactory.ModelMapperOptions.DEEP_COPY);
        return mapper.map(entity, AdministrativeUnitResponse.class);
    }

    public static AdministrativeUnitResponse of (AdministrativeUnitEntity entity, List<AdministrativeUnitEntity> parent) {
        ModelMapper mapper = MapperFactory.mapper(MapperFactory.ModelMapperOptions.DEEP_COPY);
        AdministrativeUnitResponse response = mapper.map(entity, AdministrativeUnitResponse.class);
        response.setParent(parent.stream().map(AdministrativeUnitResponse::of).collect(Collectors.toList()));
        return response;
    }
}
