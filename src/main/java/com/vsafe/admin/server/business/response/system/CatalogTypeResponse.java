package com.vsafe.admin.server.business.response.system;

import com.vsafe.admin.server.business.entities.system.CatalogTypeEntity;
import com.vsafe.admin.server.helpers.utils.mapper.MapperFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogTypeResponse implements Serializable {
    private String id;
    private String type;
    private Boolean enable = true;
    private String name;
    private String description;

    private Long count = 0L;

    public static CatalogTypeResponse of(CatalogTypeEntity entity) {
        ModelMapper mapper = MapperFactory.mapper(MapperFactory.ModelMapperOptions.DEEP_COPY);
        return mapper.map(entity, CatalogTypeResponse.class);
    }
}
