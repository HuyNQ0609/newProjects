package com.vsafe.admin.server.business.repositories.cms.system;

import com.vsafe.admin.server.business.entities.system.RoleEntity;
import com.vsafe.admin.server.business.request.system.SearchRoleRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.vsafe.admin.server.helpers.constants.SystemConstants.DEFAULT_PAGE_SIZE;

@Repository
public class RoleRepositoryCustomImpl implements IRoleRepositoryCustom {

    private final MongoTemplate mongotemplate;

    public RoleRepositoryCustomImpl(MongoTemplate mongotemplate) {
        this.mongotemplate = mongotemplate;
    }

    @Override
    public Page<RoleEntity> searchRole(SearchRoleRequest request, String clientId) {
        if (request.getPage() < 1)
            request.setPage(0);
        else
            request.setPage(request.getPage() - 1);
        if (request.getPageSize() == 0)
            request.setPageSize(DEFAULT_PAGE_SIZE);
        Pageable pageableRequest = PageRequest.of(request.getPage(), request.getPageSize());
        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(request.getName())) {
            criteria.and("name").regex(".*" + request.getName() + ".*", "i");
        }
        criteria.and("clientId").is(clientId);
        List<RoleEntity> roleEntityList = mongotemplate.find(Query.query(criteria)
                .with(Sort.by(Sort.Direction.ASC, "id")).with(pageableRequest), RoleEntity.class);
        long count = mongotemplate.count(Query.query(criteria).skip(-1).limit(-1), RoleEntity.class);
        return new PageImpl<>(roleEntityList, pageableRequest, count);
    }
}
