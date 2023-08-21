package com.vsafe.admin.server.business.services.system.implement;

import com.vsafe.admin.server.business.entities.system.subcriptions.Plan;
import com.vsafe.admin.server.business.entities.system.subcriptions.RecurringSubscription;
import com.vsafe.admin.server.business.repositories.cms.system.subscriptions.IRecurringSubscriptionRepositoryJpa;
import com.vsafe.admin.server.business.repositories.cms.system.subscriptions.ISubPlanRepositoryJpa;
import com.vsafe.admin.server.business.request.system.subscriptions.ConfigFeatureRequest;
import com.vsafe.admin.server.business.request.system.subscriptions.EditPlanRequest;
import com.vsafe.admin.server.business.request.system.subscriptions.EditRecurringSubscriptionRequest;
import com.vsafe.admin.server.business.response.system.subcriptions.PlanResponse;
import com.vsafe.admin.server.business.response.system.subcriptions.RecurringSubscriptionResponse;
import com.vsafe.admin.server.business.services.system.SubscriptionsService;
import com.vsafe.admin.server.core.exceptions.BadRequestException;
import com.vsafe.admin.server.core.exceptions.NotFoundException;
import com.vsafe.admin.server.core.exceptions.SystemException;
import com.vsafe.admin.server.helpers.enums.responseStatus.ResponseStatus;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubscriptionsServiceImpl implements SubscriptionsService {
    private final IRecurringSubscriptionRepositoryJpa recurringSubscriptionRepo;
    private final ISubPlanRepositoryJpa subPlanRepo;

    public SubscriptionsServiceImpl(
            IRecurringSubscriptionRepositoryJpa recurringSubscriptionRepo,
            ISubPlanRepositoryJpa subPlanRepo) {
        this.recurringSubscriptionRepo = recurringSubscriptionRepo;
        this.subPlanRepo = subPlanRepo;
    }

    // begin:: Subscription
    @Override
    public RecurringSubscriptionResponse addRecurringSubscription(EditRecurringSubscriptionRequest request) {
        if (recurringSubscriptionRepo.existsByCode(request.getCode())) throw new BadRequestException("Mã bảng giá đã tồn tại.");
        RecurringSubscription save = recurringSubscriptionRepo.save(EditRecurringSubscriptionRequest.getAddDocument(request));
        return RecurringSubscriptionResponse.of(save, new HashSet<>());
    }

    @Override
    public RecurringSubscriptionResponse editRecurringSubscription(EditRecurringSubscriptionRequest request) {
        if (Objects.isNull(request.getId())) throw new ValidationException("Id không được trống");
        RecurringSubscription recurringSubscription = recurringSubscriptionRepo.findById(request.getId()).orElseThrow(
                () -> new NotFoundException("Gói dịch vụ không tồn tại")
        );
        if (recurringSubscriptionRepo.existsByCodeAndIdNot(request.getCode(), request.getId())) throw new BadRequestException("Mã bảng giá đã tồn tại.");

        EditRecurringSubscriptionRequest.setEditDocument(request, recurringSubscription);
        recurringSubscriptionRepo.save(recurringSubscription);
        return RecurringSubscriptionResponse.of(recurringSubscription,  new HashSet<>());
    }

    @Override
    public RecurringSubscriptionResponse detailRecurringSubscription(String id) {
        RecurringSubscription recurringSubscription = recurringSubscriptionRepo.findById(id).orElseThrow(
                () -> new NotFoundException("Gói dịch vụ không tồn tại")
        );
        Set<PlanResponse> plans = subPlanRepo.getAllByRecurringSubscriptionId(id).stream().map(PlanResponse::of).collect(Collectors.toSet());

        return RecurringSubscriptionResponse.of(recurringSubscription, plans);
    }

    @Override
    public RecurringSubscriptionResponse getPriceTable(String id) {
        RecurringSubscription recurringSubscription = recurringSubscriptionRepo.findById(id).orElseThrow(
                () -> new NotFoundException("Gói dịch vụ không tồn tại")
        );

        Set<PlanResponse> plans = subPlanRepo.getAllByRecurringSubscriptionId(id).stream().filter(p -> p.getStatus() == 1).map(PlanResponse::of).collect(Collectors.toSet());
        return RecurringSubscriptionResponse.of(recurringSubscription, plans);
    }

    @Override
    public RecurringSubscriptionResponse getPriceTable() {
        List<RecurringSubscription> lst = recurringSubscriptionRepo.findAllByStatus(1);
        if (lst.size() > 1) throw new BadRequestException("Có nhiều hơn 1 bảng giá hoạt động");
        else {
            RecurringSubscription r = lst.get(0);
            return getPriceTable(r.getId());
        }
    }

    @Override
    public List<RecurringSubscriptionResponse> getRecurringSubscriptionList() {
        Sort sortOrder = Sort.by("createdDate").descending();
        List<RecurringSubscription> recurringSubscriptionList = recurringSubscriptionRepo.findAll(sortOrder);
        List<RecurringSubscriptionResponse> result = new ArrayList<>();
        recurringSubscriptionList.forEach(recurringSubscription -> {
            Set<PlanResponse> plans = subPlanRepo.getAllByRecurringSubscriptionId(recurringSubscription.getId()).stream().map(PlanResponse::of).collect(Collectors.toSet());
            result.add(RecurringSubscriptionResponse.of(recurringSubscription, plans));
        });
        return result;
    }

    @Override
    public void deleteRecurringSubscription(String id) {
        if(!recurringSubscriptionRepo.existsById(id)) {
            throw  new NotFoundException("Gói dịch vụ không tồn tại");
        };
        try {
            recurringSubscriptionRepo.deleteById(id);
        } catch (Exception ex) {
            throw new SystemException(ResponseStatus.SYSTEM_ERROR);
        }
    }

    @Override
    public void publishRecurringSubscription(String id) {
        if(!recurringSubscriptionRepo.existsById(id)) {
            throw  new NotFoundException("Gói dịch vụ không tồn tại");
        };
        List<RecurringSubscription> lst = recurringSubscriptionRepo.findAll();
        lst.stream().forEach(r -> {
            if (r.getId().equals(id)) r.setStatus(r.getStatus() == 0 ? 1:0);
            else r.setStatus(0);
        });
        recurringSubscriptionRepo.saveAll(lst);
    }

    @Override
    public RecurringSubscriptionResponse configFeature(ConfigFeatureRequest configFeatureRequest) {
        RecurringSubscription recurringSubscription = recurringSubscriptionRepo.findById(configFeatureRequest.getRecurringSubscriptionId()).orElseThrow(
                () -> new NotFoundException("Gói dịch vụ không tồn tại")
        );
        recurringSubscription.setFeatures(configFeatureRequest.getFeatures());
        RecurringSubscription save = recurringSubscriptionRepo.save(recurringSubscription);
        return RecurringSubscriptionResponse.of(save,  new HashSet<>());
    }


    // end:: Subscription

    @Override
    public PlanResponse addPlan(EditPlanRequest request) {
        RecurringSubscription recurringSubscription = recurringSubscriptionRepo.findById(request.getRecurringSubscriptionId()).orElseThrow(
                () -> new NotFoundException("Gói dịch vụ không tồn tại")
        );

        if (subPlanRepo.existsByCodeAndRecurringSubscriptionId(request.getCode(), request.getRecurringSubscriptionId())) {
            throw new BadRequestException("Mã Plan đã tồn tại");
        }
        Plan plan = EditPlanRequest.getAddDocument(request, recurringSubscription.getFeatures());
        return PlanResponse.of(subPlanRepo.save(plan));
    }

    @Override
    public PlanResponse editPlan(EditPlanRequest request) {
        if (Objects.isNull(request.getId())) throw new ValidationException("Id không được trống");
        if (subPlanRepo.existsByCodeAndRecurringSubscriptionIdAndIdNot(request.getCode(), request.getRecurringSubscriptionId(), request.getId()))
            throw new BadRequestException("Tên Plan đã tồn tại trên bảng giá");
        Plan plan = subPlanRepo.findById(request.getId()).orElseThrow(
                () -> new NotFoundException("Plan không tồn tại")
        );
        RecurringSubscription recurringSubscription = recurringSubscriptionRepo.findById(plan.getRecurringSubscriptionId()).orElseThrow(
                () -> new NotFoundException("Gói dịch vụ không tồn tại")
        );

        EditPlanRequest.setEditDocument(request, plan, recurringSubscription.getFeatures());
        subPlanRepo.save(plan);
        return PlanResponse.of(plan);
    }

    @Override
    public PlanResponse detailPlan(String id) {
        Plan plan = subPlanRepo.findById(id).orElseThrow(
                () -> new NotFoundException("Plan không tồn tại")
        );
        return PlanResponse.of(plan);
    }

    @Override
    public void deletePlan(String id) {
        if(!subPlanRepo.existsById(id)) {
            throw  new NotFoundException("Plan không tồn tại");
        };
        try {
            subPlanRepo.deleteById(id);
        } catch (Exception ex) {
            throw new SystemException(ResponseStatus.SYSTEM_ERROR);
        }
    }

    @Override
    public void publishPlan(String id) {
        Plan plan = subPlanRepo.findById(id).orElseThrow(
                () -> new NotFoundException("Plan không tồn tại")
        );

        List<Plan> plans = subPlanRepo.getAllByRecurringSubscriptionId(plan.getRecurringSubscriptionId());
        plans.stream().forEach(p -> {
            if (p.getId().equals(id)){
                plan.setStatus(plan.getStatus() == 0 ? 1 : 0);
                p.setStatus(p.getStatus() == 0 ? 1 : 0);
            }
        });
        long count = plans.stream().filter(p->p.getStatus() == 1).count();
        if (count > 3) throw new BadRequestException("Chỉ áp dụng tối đa 3 gói dịch vụ");
        subPlanRepo.save(plan);
    }

    @Override
    public List<PlanResponse> getPlanList(String recurringSubscriptionId) {
        List<Plan> planList = subPlanRepo.getAllByRecurringSubscriptionId(recurringSubscriptionId);
        return planList.stream().map(PlanResponse::of).collect(Collectors.toList());
    }
}
