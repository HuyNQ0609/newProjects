package com.vsafe.admin.server.business.repositories.cms.system.subscriptions;

import com.vsafe.admin.server.business.entities.system.subcriptions.RecurringSubscription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IRecurringSubscriptionRepositoryJpa extends MongoRepository<RecurringSubscription, String> {
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, String id);
    List<RecurringSubscription> findAllByStatus(int status);
}
