package com.vsafe.admin.server.business.services.opearation.implement;

import com.vsafe.admin.server.business.entities.operation.DeviceConfigEntity;
import com.vsafe.admin.server.business.entities.operation.DeviceStatusEntity;
import com.vsafe.admin.server.business.entities.operation.AreaEntity;
import com.vsafe.admin.server.business.entities.operation.LocationConfigEntity;
import com.vsafe.admin.server.business.repositories.cms.operation.*;
import com.vsafe.admin.server.business.request.operation.SearchMarkerRequest;
import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.business.response.operation.DeviceResponse;
import com.vsafe.admin.server.business.response.operation.MarkerResponse;
import com.vsafe.admin.server.business.response.system.UserDetailResponse;
import com.vsafe.admin.server.business.services.common.UserDetailService;
import com.vsafe.admin.server.business.services.opearation.GgMapService;
import com.vsafe.admin.server.helpers.constants.BusinessConstant;
import com.vsafe.admin.server.helpers.enums.UserType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class GgMapServiceImpl implements GgMapService {
    private final IMarkerRepositoryCustom iMarkerRepositoryCustom;
    private final IMarkerRepositoryJpa iMarkerRepositoryJpa;
    private final ILocationRepositoryJpa iLocationRepositoryJpa;
    private final IDeviceRepositoryJpa iDeviceRepositoryJpa;
    private final UserDetailService userDetailService;
    private final IDeviceStatusRepositoryJpa iDeviceStatusRepositoryJpa;

    public GgMapServiceImpl(IMarkerRepositoryCustom iMarkerRepositoryCustom, IMarkerRepositoryJpa iMarkerRepositoryJpa, ILocationRepositoryJpa iLocationRepositoryJpa, IDeviceRepositoryJpa iDeviceRepositoryJpa, UserDetailService userDetailService, IDeviceStatusRepositoryJpa iDeviceStatusRepositoryJpa) {
        this.iMarkerRepositoryCustom = iMarkerRepositoryCustom;
        this.iMarkerRepositoryJpa = iMarkerRepositoryJpa;
        this.iLocationRepositoryJpa = iLocationRepositoryJpa;
        this.iDeviceRepositoryJpa = iDeviceRepositoryJpa;
        this.userDetailService = userDetailService;
        this.iDeviceStatusRepositoryJpa = iDeviceStatusRepositoryJpa;
    }

    @Override
    public BaseResponse getGroupArea() {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        List<AreaEntity> groupArea = iMarkerRepositoryJpa.getGroupArea(clientId);
        return BaseResponse.success(MarkerResponse.of(groupArea));
    }

    @Override
    public BaseResponse searchWithoutPaging(SearchMarkerRequest request) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        List<AreaEntity> markerEntityList = iMarkerRepositoryCustom.searchWithoutPaging(request, clientId);
        return BaseResponse.success(MarkerResponse.of(markerEntityList));
    }

    @Override
    public BaseResponse getListLocation(String areaId) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        List<LocationConfigEntity> locationConfigEntities = iLocationRepositoryJpa.findByAreaIdAndClientIdOrderByCreatedDateDesc(areaId, clientId);
        return BaseResponse.success(locationConfigEntities);
    }

    @Override
    public BaseResponse getListDevice(String markerId) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        List<DeviceConfigEntity> deviceConfigEntities = iDeviceRepositoryJpa.findByLocationIdAndClientId(markerId, clientId);
        return BaseResponse.success(deviceConfigEntities);
    }

    @Override
    public BaseResponse getListDeviceStatusTopRank() {
        List<DeviceStatusEntity> deviceStatusEntities = iDeviceStatusRepositoryJpa.getListOrderTopRank(1);
        return BaseResponse.success(deviceStatusEntities);
    }
}
