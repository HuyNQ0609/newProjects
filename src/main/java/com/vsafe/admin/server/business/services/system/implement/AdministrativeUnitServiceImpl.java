package com.vsafe.admin.server.business.services.system.implement;

import com.vsafe.admin.server.business.entities.system.AdministrativeUnitEntity;
import com.vsafe.admin.server.business.repositories.cms.system.IAdministrativeUnitRepositoryJpa;
import com.vsafe.admin.server.business.repositories.cms.system.IAdministrativeUnitRepositoryCustom;
import com.vsafe.admin.server.business.request.system.AdministrativeUnitFilter;
import com.vsafe.admin.server.business.request.system.AdministrativeUnitRequest;
import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.business.response.system.AdministrativeUnitResponse;
import com.vsafe.admin.server.business.response.system.FlatAdministrativeUnitResponse;
import com.vsafe.admin.server.business.response.system.excel.AdministrativeUnitExcelRow;
import com.vsafe.admin.server.business.services.system.AdministrativeUnitService;
import com.vsafe.admin.server.core.exceptions.BadRequestException;
import com.vsafe.admin.server.core.exceptions.NotFoundException;
import com.vsafe.admin.server.helpers.enums.AdministrativeUnitType;
import com.vsafe.admin.server.helpers.utils.AssertUtils;
import com.vsafe.admin.server.helpers.utils.datastructure.tree.Tree;
import com.vsafe.admin.server.helpers.utils.file.ExcelUtils;
import com.vsafe.admin.server.helpers.utils.file.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vsafe.admin.server.core.exceptions.SystemException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdministrativeUnitServiceImpl implements AdministrativeUnitService {
    private final IAdministrativeUnitRepositoryJpa administrativeUnitRepo;
    private final IAdministrativeUnitRepositoryCustom administrativeUnitRepositoryCustom;

    public AdministrativeUnitServiceImpl(IAdministrativeUnitRepositoryJpa administrativeUnitRepo, IAdministrativeUnitRepositoryCustom administrativeUnitRepositoryCustom) {
        this.administrativeUnitRepositoryCustom = administrativeUnitRepositoryCustom;
        AssertUtils.defaultNotNull(administrativeUnitRepo);
        this.administrativeUnitRepo = administrativeUnitRepo;
    }

    @Override
    public void insertData(MultipartFile file) {
        try {
            List<AdministrativeUnitExcelRow> rows = ExcelUtils.readExcel(file.getInputStream(), "." + FileUtils.getExtension(file), AdministrativeUnitExcelRow.class);
            Set<AdministrativeUnitEntity> result = new HashSet<>();
            AdministrativeUnitEntity province;
            AdministrativeUnitEntity district;
            AdministrativeUnitEntity ward;
            for (AdministrativeUnitExcelRow row : rows) {
                if (StringUtils.isBlank(row.getProvinceCode())) {
                    province = new AdministrativeUnitEntity(row.getCode(), row.getName(), AdministrativeUnitType.PROVINCE.getValue(), "");
                    result.add(province);
                }
                else if (StringUtils.isBlank(row.getDistrictCode())) {
                    district = new AdministrativeUnitEntity(row.getCode(), row.getName(), AdministrativeUnitType.DISTRICT.getValue(), Tree.createTreePath(row.getProvinceCode()));
                    result.add(district);
                }
                else {
                    ward = new AdministrativeUnitEntity(row.getCode(), row.getName(), AdministrativeUnitType.WARD.getValue(), Tree.createTreePath(row.getProvinceCode(), row.getDistrictCode()));
                    result.add(ward);
                }
            }
            administrativeUnitRepo.saveAll(result);
    } catch(
    IOException e)

    {
        throw new RuntimeException(e);
    }

}

    @Override
    public Page<FlatAdministrativeUnitResponse> listAll(AdministrativeUnitFilter filter) {
        PageRequest pageAble = PageRequest.of(filter.getPageNumber() - 1, filter.getPageSize(), Sort.by("code").descending());
        Page<AdministrativeUnitEntity> lst = administrativeUnitRepo.getListByType(
                filter.getType(),
                filter.getName(),
                filter.getCode(),
                filter.getParentCode(),
                pageAble);
        List<FlatAdministrativeUnitResponse> result = lst.stream().map(entity -> {
            List<AdministrativeUnitEntity> parent = administrativeUnitRepo.findByCodeIn(StringUtils.split(entity.getPath(), ","));
            AdministrativeUnitResponse response = AdministrativeUnitResponse.of(entity, parent);
            Long districtCount = administrativeUnitRepo.countDistrict(entity.getCode());
            Long wardCount = administrativeUnitRepo.countWard(entity.getCode());
            Long villageCount = administrativeUnitRepo.countVillage(entity.getCode());
            return FlatAdministrativeUnitResponse.of(response, districtCount, wardCount, villageCount);
        }).collect(Collectors.toList());
        return new PageImpl<>(result, pageAble, lst.getTotalElements());
    }

    @Override
    public Page<AdministrativeUnitResponse> list(AdministrativeUnitFilter filter) {
        if (filter.getType() == null) throw new BadRequestException("Loại danh mục không được trống");
        PageRequest pageAble = PageRequest.of(filter.getPageNumber() - 1, filter.getPageSize(), Sort.by("code").descending());
        Page<AdministrativeUnitEntity> lst = administrativeUnitRepo.getListByType(
                filter.getType(),
                filter.getName(),
                filter.getCode(),
                filter.getParentCode(),
                pageAble);
        List<AdministrativeUnitResponse> result = lst.stream().map(entity -> {
            return AdministrativeUnitResponse.of(entity);
        }).collect(Collectors.toList());
        return new PageImpl<>(result, pageAble, lst.getTotalElements());
    }

    @Override
    public AdministrativeUnitResponse save(AdministrativeUnitRequest request) throws SystemException {
        AdministrativeUnitEntity village;
        if (request.getCode() != null) {
            village = administrativeUnitRepo.findByCodeAndType(request.getCode(), AdministrativeUnitType.VILLAGE.getValue()).orElseThrow(
                    () -> new NotFoundException("Không tìm thấy thông tin thôn/xóm.")
            );
        } else {
            village = new AdministrativeUnitEntity();
            village.setCode(genVillageCode(request.getWardCode()));
        }
        AdministrativeUnitEntity ward = administrativeUnitRepo.findByCodeAndType(request.getWardCode(), AdministrativeUnitType.WARD.getValue()).orElseThrow(
                () -> new NotFoundException("Không tìm thấy phường/ xã")
        );
        village.setName(request.getName().trim());
        village.setType(AdministrativeUnitType.VILLAGE.getValue());
        village.setPath(ward.getPath() + request.getWardCode() + ",");
        administrativeUnitRepo.save(village);

        List<AdministrativeUnitEntity> parent = administrativeUnitRepo.findByCodeIn(StringUtils.split(village.getPath(), ","));

        return AdministrativeUnitResponse.of(village, parent);
    }

    @Override
    public AdministrativeUnitResponse detail(String code) {
        AdministrativeUnitEntity entity = administrativeUnitRepo.findByCode(code).orElseThrow(
                () -> new NotFoundException("Không tìm thấy thông tin.")
        );
        List<AdministrativeUnitEntity> parent = administrativeUnitRepo.findByCodeIn(StringUtils.split(entity.getPath(), ","));
        return AdministrativeUnitResponse.of(entity, parent);
    }

    @Override
    public BaseResponse getListByTypeAndParent(Integer type, String parentCode) {
        return BaseResponse.success(administrativeUnitRepositoryCustom.getListByTypeAndParentCode(type, parentCode));
    }

    private String genVillageCode(String wardCode) throws SystemException {
        List<AdministrativeUnitEntity> childrens = administrativeUnitRepo.getChildrens(wardCode);
        List<String> codes = childrens.stream().map(AdministrativeUnitEntity::getCode).collect(Collectors.toList());
        String subCode = "";
        for (Integer i = 1; i < 1000; i++) {
            subCode = StringUtils.leftPad(i.toString(), 4, "0");
            if (!codes.contains(subCode)) break;
        }
        if (StringUtils.isBlank(subCode)) throw new SystemException("Không thể tạo code cho thôn/ xóm trên.");
        return wardCode + subCode;
    }
}
