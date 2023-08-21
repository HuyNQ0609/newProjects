package com.vsafe.admin.server.business.request.system.subscriptions;

import com.vsafe.admin.server.business.entities.system.subcriptions.Feature;
import com.vsafe.admin.server.business.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigFeatureRequest extends BaseRequest {
    private String recurringSubscriptionId;
    @Valid
    Set<Feature> features = new LinkedHashSet<>();
}
