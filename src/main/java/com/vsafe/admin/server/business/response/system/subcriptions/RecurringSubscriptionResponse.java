package com.vsafe.admin.server.business.response.system.subcriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vsafe.admin.server.business.entities.system.subcriptions.Feature;
import com.vsafe.admin.server.business.entities.system.subcriptions.RecurringSubscription;
import com.vsafe.admin.server.helpers.utils.mapper.MapperFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecurringSubscriptionResponse implements Serializable {
    private String id;
    private String name;
    private String code;
    private int status = 0;
    private int trialPeriod = 0;
    private String description;
    private Date createdDate;
    private Date updatedDate;
    private Set<String> billCycles = new HashSet<>();

    private Set<Feature> features = new LinkedHashSet<>();
    private Set<PlanResponse> plans = new HashSet<>();

    public static RecurringSubscriptionResponse of(RecurringSubscription entity, Set<PlanResponse> plans) {
        ModelMapper mapper = MapperFactory.mapper(MapperFactory.ModelMapperOptions.DEEP_COPY);
        RecurringSubscriptionResponse response = mapper.map(entity, RecurringSubscriptionResponse.class);
        response.setDescription(StringUtils.defaultString(response.getDescription(), ""));
        response.setFeatures(response.getFeatures().stream().sorted(Comparator.comparing(Feature::getOrder)).collect(Collectors.toCollection(LinkedHashSet::new)));
        response.setPlans(plans);
        return response;
    }
}
