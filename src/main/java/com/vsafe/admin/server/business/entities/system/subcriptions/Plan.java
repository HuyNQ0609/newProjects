package com.vsafe.admin.server.business.entities.system.subcriptions;

import com.vsafe.admin.server.business.entities.AuditTable;
import com.vsafe.admin.server.business.entities.BaseEntity;
import com.vsafe.admin.server.business.request.system.subscriptions.EditPlanRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.lang.model.type.ReferenceType;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Document(collection = "sys_sub_plan")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plan extends AuditTable {
    @Id
    private String id;

    @Field(targetType = FieldType.OBJECT_ID, name = "recurring_sub_id")
    private String recurringSubscriptionId;

    private String name;
    private String code;

    @Builder.Default
    private int status = 0; // 1: active, 0: inactive

    private String description;

    @Builder.Default
    private int order=0;

    private boolean recommended;

    @Field(name = "is_free_plan")
    private boolean freePlan;

    @Builder.Default
    private Set<Feature> features = new HashSet<>();
    @Builder.Default
    private Set<Price> prices = new HashSet<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Feature {
        @Field(name = "feature_key")
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Price {
        @Field(name = "old_price")
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
}
