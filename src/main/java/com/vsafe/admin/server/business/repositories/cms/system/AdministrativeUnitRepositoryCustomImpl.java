package com.vsafe.admin.server.business.repositories.cms.system;

import com.vsafe.admin.server.business.entities.system.AdministrativeUnitEntity;
import com.vsafe.admin.server.business.response.system.FlatAdministrativeUnitResponse;
import com.vsafe.admin.server.helpers.utils.AssertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class AdministrativeUnitRepositoryCustomImpl implements IAdministrativeUnitRepositoryCustom {
    private final MongoTemplate mongotemplate;

    public AdministrativeUnitRepositoryCustomImpl(MongoTemplate mongotemplate) {
        AssertUtils.defaultNotNull(mongotemplate);
        this.mongotemplate = mongotemplate;
    }

    @Override
    public List<AdministrativeUnitEntity> list(String name) {
        Criteria criteria = Criteria.where("name").regex(".*" + (StringUtils.isNotBlank(name) ? name : "") + ".*", "i");
        Query query = new Query();
        query.addCriteria(criteria);
        return mongotemplate.find(query, AdministrativeUnitEntity.class);
    }

    @Override
    public List<FlatAdministrativeUnitResponse> flatList(String name) {
        return null;
    }

    @Override
    public List<AdministrativeUnitEntity> getListByTypeAndParentCode(Integer type, String parentCode) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("type").is(type), Criteria.where("path").regex(StringUtils.isNotBlank(parentCode) ? "," + parentCode + ",$" : "", "i"));
        return mongotemplate.find(Query.query(criteria).with(Sort.by(Sort.Direction.ASC, "code")), AdministrativeUnitEntity.class);
    }
}
