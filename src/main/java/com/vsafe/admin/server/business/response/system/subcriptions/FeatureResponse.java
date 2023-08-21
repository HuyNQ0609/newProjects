package com.vsafe.admin.server.business.response.system.subcriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vsafe.admin.server.business.entities.system.subcriptions.Feature;
import com.vsafe.admin.server.helpers.utils.mapper.MapperFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeatureResponse implements Serializable {
    private String id;
    private String recurringSubscriptionId;
    private String name;
    private boolean visible;
    private boolean checked;
    private boolean canCustomName;

    private int order = 0;

    public static FeatureResponse of (Feature entity) {
        ModelMapper mapper = MapperFactory.mapper(MapperFactory.ModelMapperOptions.DEEP_COPY);
        return mapper.map(entity, FeatureResponse.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeatureResponse that = (FeatureResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(recurringSubscriptionId, that.recurringSubscriptionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recurringSubscriptionId);
    }
}
