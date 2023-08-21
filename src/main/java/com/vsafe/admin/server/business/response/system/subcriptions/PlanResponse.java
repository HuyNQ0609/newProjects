package com.vsafe.admin.server.business.response.system.subcriptions;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vsafe.admin.server.business.entities.system.subcriptions.Plan;
import com.vsafe.admin.server.helpers.utils.mapper.MapperFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanResponse implements Serializable {
    private String id;
    private String recurringSubscriptionId;
    private String name;
    private String code;
    private int status; // 1: active, 0: inactive
    private String description;
    private int order;
    private boolean recommended;
    private boolean freePlan;

    private Set<Feature> features = new HashSet<>();
    private Set<Price> prices = new HashSet<>();

    private Date createdDate;
    private Date updatedDate;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Feature {
        private String featureKey;
        private String caption;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Feature feature = (Feature) o;
            return Objects.equals(featureKey, feature.featureKey);
        }

        @Override
        public int hashCode() {
            return Objects.hash(featureKey);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Price {
        private Long oldPrice;
        private Long price;
        private String type;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Price price = (Price) o;
            return Objects.equals(type, price.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type);
        }
    }

    public static PlanResponse of (Plan entity) {
        ModelMapper mapper = MapperFactory.mapper(MapperFactory.ModelMapperOptions.DEEP_COPY);
        PlanResponse response = mapper.map(entity, PlanResponse.class);
        response.setDescription(StringUtils.defaultString(response.getDescription(), ""));
        response.setFeatures(entity.getFeatures().stream().map(feature -> mapper.map(feature, Feature.class)).collect(Collectors.toSet()));
        response.setPrices(entity.getPrices().stream().map(price -> mapper.map(price, Price.class)).collect(Collectors.toSet()));
        return response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanResponse response = (PlanResponse) o;
        return Objects.equals(id, response.id) && Objects.equals(recurringSubscriptionId, response.recurringSubscriptionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recurringSubscriptionId);
    }
}
