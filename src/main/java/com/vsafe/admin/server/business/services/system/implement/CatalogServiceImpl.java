package com.vsafe.admin.server.business.services.system.implement;

import com.vsafe.admin.server.business.entities.system.CatalogEntity;
import com.vsafe.admin.server.business.entities.system.CatalogTypeEntity;
import com.vsafe.admin.server.business.repositories.cms.system.ICatalogRepository;
import com.vsafe.admin.server.business.repositories.cms.system.ICatalogTypeRepository;
import com.vsafe.admin.server.business.request.system.CatalogFilter;
import com.vsafe.admin.server.business.request.system.CatalogRequest;
import com.vsafe.admin.server.business.request.system.CatalogTypeRequest;
import com.vsafe.admin.server.business.response.system.CatalogResponse;
import com.vsafe.admin.server.business.response.system.CatalogTypeResponse;
import com.vsafe.admin.server.business.services.system.CatalogService;
import com.vsafe.admin.server.core.exceptions.BadRequestException;
import com.vsafe.admin.server.core.exceptions.NotFoundException;
import com.vsafe.admin.server.helpers.utils.AssertUtils;
import com.vsafe.admin.server.helpers.utils.datastructure.tree.Tree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.vsafe.admin.server.core.exceptions.SystemException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CatalogServiceImpl implements CatalogService {

    private final ICatalogTypeRepository catalogTypeRepo;
    private final ICatalogRepository catalogRepo;

    public CatalogServiceImpl(ICatalogTypeRepository catalogTypeRepo, ICatalogRepository catalogRepo) {
        AssertUtils.defaultNotNull(catalogRepo);
        AssertUtils.defaultNotNull(catalogTypeRepo);
        this.catalogTypeRepo = catalogTypeRepo;
        this.catalogRepo = catalogRepo;
    }

    @Override
    @Transactional
    public CatalogTypeEntity save(CatalogTypeRequest request) throws SystemException {
        CatalogTypeEntity entity;
        List<CatalogEntity> catalogEntityList = new ArrayList<>();
        if (request.getId() != null) {
            entity = catalogTypeRepo.findById(request.getId()).orElseThrow(
                    () -> new NotFoundException("Không tìm thấy loại Catalog")
            );

            catalogEntityList = catalogRepo.findAllByCatalogType_Type(entity.getType());

        }else {
            entity = new CatalogTypeEntity();
            if (catalogTypeRepo.existsByType(request.getType())) {
                throw new SystemException("Loại Catalog đã được cấu hình.");
            }
        }
        if (catalogTypeRepo.existsByIdNotAndType(request.getId(), request.getType())) {
            throw new SystemException("Loại Catalog đã được cấu hình.");
        }
        if (catalogTypeRepo.existsByIdNotAndName(request.getId(), request.getName())) {
            throw new SystemException("Trùng tên loại Catalog");
        }
        entity.setName(request.getName());
        entity.setType(request.getType());
        entity.setEnable(request.getEnable());
        entity.setDescription(request.getDescription());
        catalogTypeRepo.save(entity);

        catalogEntityList.forEach(catalogEntity -> catalogEntity.setCatalogType(entity));
        catalogRepo.saveAll(catalogEntityList);
        return entity;
    }

    @Override
    public CatalogEntity save(CatalogRequest request) throws SystemException {
        CatalogEntity entity;
        CatalogEntity parentEntity;
        if (request.getId() != null) {
            entity = catalogRepo.findById(request.getId()).orElseThrow(
                    () -> new NotFoundException("Không tìm thấy Catalog")
            );

        }else entity = new CatalogEntity();
        if (catalogRepo.existsByNameAndCatalogType_TypeAndIdNot(request.getName(), request.getCatalogType(), request.getId())) {
            throw new SystemException("Trùng tên Catalog");
        }
        entity.setId(request.getId());
        entity.setName(request.getName());
        entity.setEnable(request.getEnable());
        entity.setDescription(request.getDescription());
        entity.setPriority(request.getPriority());

        CatalogTypeEntity catalogTypeEntity = catalogTypeRepo.findByType(request.getCatalogType()).orElseThrow(
                () -> new NotFoundException("không tìm thấy loại Catalog")
        );
        entity.setCatalogType(catalogTypeEntity);

        if (request.getParentId() != null) {
            parentEntity = catalogRepo.findById(request.getParentId()).orElseThrow(
                    () -> new NotFoundException("Không tìm thấy Catalog cha")
            );
            if (!parentEntity.getCatalogType().getType().equals(request.getCatalogType()))
                throw new BadRequestException("Catalog cha không hợp lệ");
            entity.setParentId(request.getParentId());
        }
        catalogRepo.save(entity);
        return entity;
    }

    @Override
    public List<CatalogTypeResponse> getCatalogTypeALl() {
        List<CatalogTypeEntity> lst = catalogTypeRepo.findAll();
        List<CatalogTypeResponse> result = lst.stream().map(entity -> {
            CatalogTypeResponse response = CatalogTypeResponse.of(entity);
            Long count = catalogRepo.countByCatalogType_Type(entity.getType());
            response.setCount(count);
            return response;
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<CatalogResponse> getCatalogList(CatalogFilter filter) {
        List<CatalogEntity> catalogList = catalogRepo.getCatalogList(filter.getName(), filter.getCatalogTypeId());
        List<CatalogResponse> responseList = catalogList.stream().map(CatalogResponse::of).collect(Collectors.toList());
        return Tree.createTreeSet(responseList);
    }

    @Override
    @Transactional
    public void deleteCatalogType(String id) {
        CatalogTypeEntity entity = catalogTypeRepo.findById(id).orElseThrow(
                () -> new NotFoundException("Không tìm thấy loại Catalog")
        );
        List<CatalogEntity> catalogEntityList = catalogRepo.findAllByCatalogType_Type(entity.getId());
        catalogEntityList.forEach(catalogEntity -> catalogEntity.setCatalogType(null));
        catalogTypeRepo.deleteById(id);
        catalogRepo.deleteAll(catalogEntityList);
    }

    @Override
    public CatalogTypeEntity changeCatalogTypeState(String catalogTypeId) {
        CatalogTypeEntity entity = catalogTypeRepo.findById(catalogTypeId).orElseThrow(
                () -> new NotFoundException("Không tìm thấy loại Catalog")
        );
        entity.setEnable(!entity.getEnable());
        return catalogTypeRepo.save(entity);
    }

    @Override
    public CatalogEntity changeCatalogState(String catalogTypeId) {
        CatalogEntity entity = catalogRepo.findById(catalogTypeId).orElseThrow(
                () -> new NotFoundException("Không tìm thấy Catalog")
        );
        entity.setEnable(!entity.getEnable());
        return catalogRepo.save(entity);
    }
}
