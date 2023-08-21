package com.vsafe.admin.server.business.services.system;

import com.vsafe.admin.server.business.request.system.subscriptions.ConfigFeatureRequest;
import com.vsafe.admin.server.business.request.system.subscriptions.EditPlanRequest;
import com.vsafe.admin.server.business.request.system.subscriptions.EditRecurringSubscriptionRequest;
import com.vsafe.admin.server.business.response.system.subcriptions.FeatureResponse;
import com.vsafe.admin.server.business.response.system.subcriptions.PlanResponse;
import com.vsafe.admin.server.business.response.system.subcriptions.RecurringSubscriptionResponse;

import java.util.List;

public interface SubscriptionsService {
    // begin:: Subscription
    RecurringSubscriptionResponse addRecurringSubscription(EditRecurringSubscriptionRequest request);
    RecurringSubscriptionResponse editRecurringSubscription(EditRecurringSubscriptionRequest request);
    RecurringSubscriptionResponse detailRecurringSubscription(String id);
    RecurringSubscriptionResponse getPriceTable(String id);
    RecurringSubscriptionResponse getPriceTable();
    List<RecurringSubscriptionResponse> getRecurringSubscriptionList();
    void deleteRecurringSubscription(String id);
    void publishRecurringSubscription(String id);
    RecurringSubscriptionResponse configFeature(ConfigFeatureRequest features);
    // end:: Subscription


    PlanResponse addPlan(EditPlanRequest request);
    PlanResponse editPlan(EditPlanRequest request);
    PlanResponse detailPlan(String id);
    void deletePlan(String id);
    void publishPlan(String id);
    List<PlanResponse> getPlanList(String recurringSubscriptionId);
}
