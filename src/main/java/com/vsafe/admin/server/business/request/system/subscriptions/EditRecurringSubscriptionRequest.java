package com.vsafe.admin.server.business.request.system.subscriptions;

import com.vsafe.admin.server.business.entities.system.subcriptions.RecurringSubscription;
import com.vsafe.admin.server.core.exceptions.BadRequestException;
import com.vsafe.admin.server.core.exceptions.SystemException;
import com.vsafe.admin.server.helpers.enums.BaseStatus;
import com.vsafe.admin.server.helpers.enums.RecurringSubscriptionEnum;
import com.vsafe.admin.server.helpers.validators.ValueOfEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditRecurringSubscriptionRequest{
    private String id;

    @NotBlank()
    @Size(max = 64)
    private String name;

    @NotBlank()
    @Size(max = 128)
    @Pattern(regexp = "^[a-zA-Z0-9_\\-]*")
    private String code;

    @Min(value = 0)
    private int trialPeriod = 0;

    @Size(max = 512)
    private String description;

    private Set<String> billCycles = new HashSet<>();

    public static RecurringSubscription getAddDocument(EditRecurringSubscriptionRequest request) {

        return RecurringSubscription.builder()
                .name(request.getName())
                .code(request.getCode())
                .status(0)
                .trialPeriod(request.getTrialPeriod())
                .description(request.getDescription())
                .billCycles(request.getBillCycles().stream().filter(s -> Objects.nonNull(RecurringSubscriptionEnum.BillCycle.parse(s))).collect(Collectors.toSet()))
                .build();
    }

    public static void setEditDocument(EditRecurringSubscriptionRequest request, RecurringSubscription recurringSubscription) {
        recurringSubscription.setName(request.getName());
        recurringSubscription.setCode(request.getCode());
        recurringSubscription.setTrialPeriod(request.getTrialPeriod());
        recurringSubscription.setDescription(request.getDescription());
        recurringSubscription.setBillCycles(request.getBillCycles().stream().filter(s -> Objects.nonNull(RecurringSubscriptionEnum.BillCycle.parse(s))).collect(Collectors.toSet()));
    }
}
