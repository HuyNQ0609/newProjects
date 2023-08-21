package com.vsafe.admin.server.business.repositories.cms.system.subscriptions;

import com.vsafe.admin.server.business.entities.system.subcriptions.Plan;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ISubPlanRepositoryJpa extends MongoRepository<Plan, String> {
    List<Plan> getAllByRecurringSubscriptionId(String recurringSubscriptionId);
    boolean existsByCodeAndRecurringSubscriptionId(String code, String recurringSubscriptionId);
    boolean existsByCodeAndRecurringSubscriptionIdAndIdNot(String code, String recurringSubscriptionId, String id);
}
