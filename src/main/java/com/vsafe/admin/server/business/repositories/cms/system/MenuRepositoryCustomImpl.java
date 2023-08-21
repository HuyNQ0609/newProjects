package com.vsafe.admin.server.business.repositories.cms.system;

import com.vsafe.admin.server.business.entities.system.MenuEntity;
import com.vsafe.admin.server.business.request.system.SearchMenuRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class MenuRepositoryCustomImpl implements IMenuRepositoryCustom {

    private final MongoTemplate mongotemplate;

    public MenuRepositoryCustomImpl(MongoTemplate mongotemplate) {
        this.mongotemplate = mongotemplate;
    }

    @Override
    public List<MenuEntity> searchWithoutPaging(SearchMenuRequest request) {
        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(request.getName())) {
            criteria.and("name").regex(".*" + request.getName() + ".*", "i");
        }
        if (!StringUtils.isEmpty(request.getParentId())) {
            criteria.and("parentId").is(request.getParentId());
        }
        if (request.getLevel() != null) {
            criteria.and("level").is(request.getLevel());
        }
        if (request.getIsVisible() != null) {
            criteria.and("is_visible").is(request.getIsVisible() ? 1 : 0);
        }
        return mongotemplate.find(Query.query(criteria)
                .with(Sort.by(Sort.Direction.ASC, "level")
                        .and(Sort.by(Sort.Direction.DESC, "order"))
                        .and(Sort.by(Sort.Direction.ASC, "pathId"))), MenuEntity.class);
    }

    @Override
    public List<MenuEntity> searchWithoutPaging(String request) {
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("name").regex(".*" + request + ".*", "i"),
                Criteria.where("description").regex(".*" + request + ".*", "i"),
                Criteria.where("id").regex(".*" + request + ".*", "i"));
        return mongotemplate.find(Query.query(criteria)
                .with(Sort.by(Sort.Direction.ASC, "level")
                        .and(Sort.by(Sort.Direction.DESC, "order"))
                        .and(Sort.by(Sort.Direction.ASC, "pathId"))), MenuEntity.class);
    }

    @Override
    public List<MenuEntity> findByRoleIdInAndClientIdAndStatus(List<String> roleIds, String clientId, List<Integer> status) {
        Criteria criteria = new Criteria();
        if (roleIds != null && roleIds.size() > 0) {
            criteria.and("permission.roleId").in(roleIds);
        }
        criteria.and("clientId").is(clientId);
        criteria.and("status").in(status);
        return mongotemplate.find(Query.query(criteria)
                .with(Sort.by(Sort.Direction.ASC, "level")
                        .and(Sort.by(Sort.Direction.DESC, "order"))
                        .and(Sort.by(Sort.Direction.ASC, "pathId"))), MenuEntity.class);
    }

    @Override
    public List<MenuEntity> findByIdInAndClientId(Set<String> ids, String clientId) {
        Criteria criteria = new Criteria();
        if (ids != null && ids.size() > 0) {
            criteria.and("id").in(ids);
            criteria.and("clientId").is(clientId);
        }
        return mongotemplate.find(Query.query(criteria)
                .with(Sort.by(Sort.Direction.ASC, "level")
                        .and(Sort.by(Sort.Direction.DESC, "order"))
                        .and(Sort.by(Sort.Direction.ASC, "pathId"))), MenuEntity.class);
    }

}
