package com.vsafe.admin.server.business.controllers.system;

import com.vsafe.admin.server.business.request.system.subscriptions.ConfigFeatureRequest;
import com.vsafe.admin.server.business.request.system.subscriptions.EditPlanRequest;
import com.vsafe.admin.server.business.request.system.subscriptions.EditRecurringSubscriptionRequest;
import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.business.services.system.SubscriptionsService;
import com.vsafe.admin.server.core.annotations.TrackExecutionTime;
import com.vsafe.admin.server.core.configurations.ResourcePath;
import com.vsafe.admin.server.helpers.utils.AssertUtils;
import com.vsafe.admin.server.helpers.validators.groups.Crud;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(ResourcePath.BASE + "/recurring-subscription")
@Validated
public class RecurringSubscriptionController {
    private final SubscriptionsService subscriptionsService;

    public RecurringSubscriptionController(SubscriptionsService subscriptionsService) {
        AssertUtils.defaultNotNull(subscriptionsService);
        this.subscriptionsService = subscriptionsService;
    }

    @GetMapping("/get-price-table")
    public ResponseEntity<?> getPriceTable() {
        return new ResponseEntity<>(BaseResponse.success(subscriptionsService.getPriceTable()), HttpStatus.OK);
    }

    @GetMapping("/{id}/get-price-table")
    public ResponseEntity<?> getPriceTable(@PathVariable("id") String id) {
        return new ResponseEntity<>(BaseResponse.success(subscriptionsService.getPriceTable(id)), HttpStatus.OK);
    }

    // BEGIN:: recurring-subscription
    @PostMapping("/add")
    public ResponseEntity<?> addRecurringSubscription(@RequestBody @Validated(Crud.Add.class) EditRecurringSubscriptionRequest request) {
        return new ResponseEntity<>(BaseResponse.success(subscriptionsService.addRecurringSubscription(request)), HttpStatus.OK);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editRecurringSubscription(@RequestBody @Validated(Crud.Edit.class) EditRecurringSubscriptionRequest request) {
        return new ResponseEntity<>(BaseResponse.success(subscriptionsService.editRecurringSubscription(request)), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detailRecurringSubscription(@PathVariable("id") String id) {
        return new ResponseEntity<>(BaseResponse.success(subscriptionsService.detailRecurringSubscription(id)), HttpStatus.OK);
    }

    @PostMapping("/publish/{id}")
    public ResponseEntity<?> publishRecurringSubscription(@PathVariable("id") String id) {
        subscriptionsService.publishRecurringSubscription(id);
        return new ResponseEntity<>(BaseResponse.success("Thành công"), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecurringSubscription(@PathVariable("id") String id) {
        subscriptionsService.deleteRecurringSubscription(id);
        return new ResponseEntity<>(BaseResponse.success("Thành công"), HttpStatus.OK);
    }

    @PostMapping("/{id}/config-features")
    public ResponseEntity<?> configFeature(@PathVariable("id") String id, @Valid @RequestBody ConfigFeatureRequest config) {
        config.setRecurringSubscriptionId(id);
        return new ResponseEntity<>(BaseResponse.success(subscriptionsService.configFeature(config)), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> listRecurringSubscription() {
        return new ResponseEntity<>(BaseResponse.success(subscriptionsService.getRecurringSubscriptionList()), HttpStatus.OK);
    }
    // END:: recurring-subscription

    // BEGIN:: Plan
    @PostMapping("/plan/add")
    @TrackExecutionTime
    public ResponseEntity<?> addPlan(@RequestBody @Valid @Validated(Crud.Add.class) EditPlanRequest request) {
        return new ResponseEntity<>(BaseResponse.success(subscriptionsService.addPlan(request)), HttpStatus.OK);
    }

    @PostMapping("/plan/edit")
    @TrackExecutionTime
    public ResponseEntity<?> editPlan(@RequestBody @Valid @Validated(Crud.Edit.class) EditPlanRequest request) {
        return new ResponseEntity<>(BaseResponse.success(subscriptionsService.editPlan(request)), HttpStatus.OK);
    }

    @GetMapping("/plan/detail/{id}")
    @TrackExecutionTime
    public ResponseEntity<?> detailPlan(@PathVariable("id") String id) {
        return new ResponseEntity<>(BaseResponse.success(subscriptionsService.detailPlan(id)), HttpStatus.OK);
    }

    @PostMapping("/plan/publish/{id}")
    public ResponseEntity<?> publishPlan(@PathVariable("id") String id) {
        subscriptionsService.publishPlan(id);
        return new ResponseEntity<>(BaseResponse.success("Thành công"), HttpStatus.OK);
    }

    @DeleteMapping("/plan/{id}")
    @TrackExecutionTime
    public ResponseEntity<?> deletePlan(@PathVariable("id") String id) {
        subscriptionsService.deletePlan(id);
        return new ResponseEntity<>(BaseResponse.success("Thành công"), HttpStatus.OK);
    }

    @GetMapping("{recurringSubId}/plan/list")
    @TrackExecutionTime
    public ResponseEntity<?> listPlan(@PathVariable("recurringSubId") String id) {
        return new ResponseEntity<>(BaseResponse.success(subscriptionsService.getPlanList(id)), HttpStatus.OK);
    }
    // END:: Plan
}
