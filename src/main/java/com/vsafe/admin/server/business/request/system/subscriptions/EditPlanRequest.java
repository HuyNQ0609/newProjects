package com.vsafe.admin.server.business.request.system.subscriptions;

import com.vsafe.admin.server.business.entities.system.subcriptions.Feature;
import com.vsafe.admin.server.business.entities.system.subcriptions.Plan;
import com.vsafe.admin.server.core.exceptions.NotFoundException;
import com.vsafe.admin.server.helpers.constants.enums.subcriptions.ActiveStatusType;
import com.vsafe.admin.server.helpers.enums.RecurringSubscriptionEnum;
import com.vsafe.admin.server.helpers.validators.ValueOfEnum;
import com.vsafe.admin.server.helpers.validators.groups.Crud;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class EditPlanRequest {
    private String id;

    @NotBlank
    private String recurringSubscriptionId;

    @NotBlank
    @Size(max = 32)
    private String name;

    @NotBlank
    @Size(max = 64)
    @Pattern(regexp = "^[a-zA-Z0-9_\\-]*")
    private String code;

    @Size(max = 256)
    private String description;

    private int order = 0;

    private boolean recommended = false;

    private boolean freePlan = false;

    private Set<Price> prices = new HashSet<>();
    private Set<PlanFeature> features = new LinkedHashSet<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Valid
    public static class Price {
        private Long oldPrice;
        @NotNull
        @Min(value = 0)
        private Long price;
        @NotBlank
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Valid
    public static class PlanFeature {
        @NotNull
        private String featureKey;
        private String caption;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PlanFeature feature = (PlanFeature) o;
            return Objects.equals(featureKey, feature.featureKey);
        }

        @Override
        public int hashCode() {
            return Objects.hash(featureKey);
        }
    }

    public static Plan getAddDocument(EditPlanRequest request, Set<Feature> features) {
        Set<Plan.Price> prices = request.getPrices().stream().map(s -> new Plan.Price(s.oldPrice, s.price, s.type)).collect(Collectors.toSet());
        processFeatureSetInPlan(request.getFeatures(), features);
        return Plan.builder()
                .recurringSubscriptionId(request.getRecurringSubscriptionId().trim())
                .name(request.getName().trim())
                .code(request.getCode().trim())
                .status(0)
                .description(StringUtils.trimToNull(request.getDescription()))
                .order(request.getOrder())
                .freePlan(request.isFreePlan())
                .recommended(request.isRecommended())
                .prices(prices.stream().filter(s -> Objects.nonNull(RecurringSubscriptionEnum.BillCycle.parse(s.getType()))).collect(Collectors.toSet()))
                .features(request.getFeatures().stream().map(f -> new Plan.Feature(f.getFeatureKey(), f.getCaption())).collect(Collectors.toSet()))
                .build();
    }

    public static void setEditDocument(EditPlanRequest request, Plan plan, Set<Feature> features) {
        Set<Plan.Price> prices = request.getPrices().stream().map(s -> new Plan.Price(s.oldPrice, s.price, s.type)).collect(Collectors.toSet());
        processFeatureSetInPlan(request.getFeatures(), features);
        plan.setName(request.getName().trim());
        plan.setCode(request.getCode().trim());
        plan.setDescription(StringUtils.trimToNull(request.getDescription()));
        plan.setOrder(request.getOrder());
        plan.setFreePlan(request.isFreePlan());
        plan.setRecommended(request.isRecommended());
        plan.setPrices(prices.stream().filter(s -> Objects.nonNull(RecurringSubscriptionEnum.BillCycle.parse(s.getType()))).collect(Collectors.toSet()));
        plan.setFeatures(request.getFeatures().stream().map(f -> new Plan.Feature(f.getFeatureKey(), f.getCaption())).collect(Collectors.toSet()));
    }

    public static void processFeatureSetInPlan(Set<PlanFeature> planFeatures, Set<Feature> features) {
        Map<String, Feature> featureMap = features.stream().collect(
                Collectors.toMap(Feature::getKey, Function.identity())
        );
        List<String> removes = new ArrayList<>();
        for (PlanFeature feature : planFeatures) {
            if (!featureMap.containsKey(feature.getFeatureKey()))
                removes.add(feature.getFeatureKey());
        }
        planFeatures.removeIf(f -> removes.contains(f.getFeatureKey()));
    }

}
