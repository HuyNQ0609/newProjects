package com.vsafe.admin.server.business.entities.system.subcriptions;

import com.vsafe.admin.server.business.entities.AuditTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.*;

@Document(collection = "sys_recurring_sub")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecurringSubscription extends AuditTable {
    @Id
    private String id;
    private String name;
    private String code;
    @Builder.Default
    private int status=0; // 1: active, 0: inactive
    private String description;

    @Field(name = "trial_period")
    @Builder.Default
    private int trialPeriod = 0;

    @Field(name="bill_cycle")
    @Builder.Default
    private Set<String> billCycles = new HashSet<>();

    @Field(name="features")
    @Builder.Default
    private Set<Feature> features = new LinkedHashSet<>();
}
