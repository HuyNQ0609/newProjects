package com.vsafe.admin.server.business.response.system;

import com.vsafe.admin.server.helpers.enums.AdministrativeUnitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlatAdministrativeUnitResponse {
    String name;
    String code;
    Integer type;

    String path;
    String provinceCode;
    String provinceName;
    String districtCode;
    String districtName;
    String wardCode;
    String wardName;
    String villageCode;
    String villageName;

    Long provinceCount;
    Long districtCount;

    Long wardCount;

    Long villageCount;

    public static FlatAdministrativeUnitResponse of(AdministrativeUnitResponse unitResponse) {
        FlatAdministrativeUnitResponse flatResponse = new FlatAdministrativeUnitResponse();
        List<AdministrativeUnitResponse> lst = unitResponse.getParent();
        flatResponse.setName(unitResponse.getName());
        flatResponse.setCode(unitResponse.getCode());
        flatResponse.setType(unitResponse.getType());
        flatResponse.setPath(unitResponse.getPath());
        lst.add(unitResponse);
        for (AdministrativeUnitResponse unit : lst) {
            int type = unit.getType();
            if (AdministrativeUnitType.PROVINCE.isEqual(type)) {
                flatResponse.setProvinceCode(unit.getCode());
                flatResponse.setProvinceName(unit.getName());
            } else if (AdministrativeUnitType.DISTRICT.isEqual(type)) {
                flatResponse.setDistrictCode(unit.getCode());
                flatResponse.setDistrictName(unit.getName());
            } else if (AdministrativeUnitType.WARD.isEqual(type)) {
                flatResponse.setWardCode(unit.getCode());
                flatResponse.setWardName(unit.getName());
            } else if (AdministrativeUnitType.VILLAGE.isEqual(type)) {
                flatResponse.setVillageCode(unit.getCode());
                flatResponse.setVillageName(unit.getName());
            } else break;
        }

        if (AdministrativeUnitType.PROVINCE.isEqual(unitResponse.getType())) {
            flatResponse.setProvinceCode(null);
            flatResponse.setProvinceName(null);
        } else if (AdministrativeUnitType.DISTRICT.isEqual(unitResponse.getType())) {
            flatResponse.setDistrictCode(null);
            flatResponse.setDistrictName(null);
        } else if (AdministrativeUnitType.WARD.isEqual(unitResponse.getType())) {
            flatResponse.setWardCode(null);
            flatResponse.setWardName(null);
        } else if (AdministrativeUnitType.VILLAGE.isEqual(unitResponse.getType())) {
            flatResponse.setVillageCode(null);
            flatResponse.setVillageName(null);
        }
        return flatResponse;
    }

    public static FlatAdministrativeUnitResponse of(AdministrativeUnitResponse unitResponse, Long districtCount, Long wardCount, Long villageCount) {
        FlatAdministrativeUnitResponse flatResponse = of(unitResponse);
        flatResponse.setDistrictCount(districtCount);
        flatResponse.setWardCount(wardCount);
        flatResponse.setVillageCount(villageCount);
        return flatResponse;
    }

}
