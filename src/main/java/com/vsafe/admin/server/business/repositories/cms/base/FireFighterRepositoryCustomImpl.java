package com.vsafe.admin.server.business.repositories.cms.base;

import com.vsafe.admin.server.business.entities.base.FireFighterEntity;
import com.vsafe.admin.server.business.request.base.SearchFireFighterRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.vsafe.admin.server.helpers.constants.SystemConstants.DEFAULT_PAGE_NUMBER;
import static com.vsafe.admin.server.helpers.constants.SystemConstants.DEFAULT_PAGE_SIZE;

@Repository
public class FireFighterRepositoryCustomImpl implements IFireFighterRepositoryCustom {

    private final MongoTemplate mongotemplate;

    public FireFighterRepositoryCustomImpl(MongoTemplate mongotemplate) {
        this.mongotemplate = mongotemplate;
    }

    @Override
    public long countSearch(SearchFireFighterRequest request) {
        return 0;
    }

    @Override
    public List<FireFighterEntity> searchWithPaging(SearchFireFighterRequest request) {
        Pageable paging = PageRequest.of(request.getPageNumber() <= 0 ? DEFAULT_PAGE_NUMBER : request.getPageNumber() - 1, request.getPageSize() == 0 ? DEFAULT_PAGE_SIZE : request.getPageSize());

        Criteria criteria = Criteria.where("level").is(request.getLevel());
        if (!StringUtils.isEmpty(request.getOrgName())) {
            criteria.and("orgName").regex(".*" + request.getOrgName() + ".*", "i");
        }
        if (!StringUtils.isEmpty(request.getParentId())) {
            criteria.and("parentId").is(request.getParentId());
        }
        if (request.getProvinceId() != null) {
            criteria.and("provinceId").is(request.getProvinceId());
        }
        if (request.getDistrictId() != null) {
            criteria.and("districtId").is(request.getDistrictId());
        }
        return mongotemplate.find(Query.query(criteria).with(paging), FireFighterEntity.class);
    }

    @Override
    public List<FireFighterEntity> searchWithoutPaging(SearchFireFighterRequest request) {

        Criteria criteria = Criteria.where("level").is(request.getLevel());
        if (!StringUtils.isEmpty(request.getOrgName())) {
            criteria.and("orgName").regex(".*" + request.getOrgName() + ".*", "i");
        }
        if (!StringUtils.isEmpty(request.getParentId())) {
            criteria.and("parentId").is(request.getParentId());
        }
        if (request.getProvinceId() != null) {
            criteria.and("provinceId").is(request.getProvinceId());
        }
        if (request.getDistrictId() != null) {
            criteria.and("districtId").is(request.getDistrictId());
        }
        return mongotemplate.find(Query.query(criteria), FireFighterEntity.class);
    }
}