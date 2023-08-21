package com.vsafe.admin.server.business.repositories.cms.system;

import com.vsafe.admin.server.business.entities.system.SystemConfigEntity;
import com.vsafe.admin.server.business.request.system.SearchSystemConfigRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.vsafe.admin.server.helpers.constants.SystemConstants.DEFAULT_PAGE_NUMBER;
import static com.vsafe.admin.server.helpers.constants.SystemConstants.DEFAULT_PAGE_SIZE;

@Repository
public class SystemConfigRepositoryCustomImpl implements ISystemConfigRepositoryCustom {

    private final MongoTemplate mongotemplate;

    public SystemConfigRepositoryCustomImpl(MongoTemplate mongotemplate) {
        this.mongotemplate = mongotemplate;
    }

    @Override
    public long countSearch(SearchSystemConfigRequest request, String clientId) {
        String title = request.getTitle();
        Criteria criteria = Criteria.where("clientId").is(clientId);

        if (StringUtils.isNotEmpty(title)) {
            if (StringUtils.isNotEmpty(title)) {
                criteria.andOperator(criteria.orOperator(Criteria.where("code").regex(".*" + title + ".*", "i"),
                        Criteria.where("description").regex(".*" + title + ".*", "i")));
            }
        }
        long total = mongotemplate.count(Query.query(criteria), SystemConfigEntity.class);
        return total;
    }

    @Override
    public List<SystemConfigEntity> searchWithPaging(SearchSystemConfigRequest request, String clientId) {
        Pageable paging = PageRequest.of(request.getPageNumber() <= 0 ? DEFAULT_PAGE_NUMBER : request.getPageNumber() - 1, request.getPageSize() == 0 ? DEFAULT_PAGE_SIZE
                : request.getPageSize());

        String title = request.getTitle();

        Criteria criteria = Criteria.where("clientId").is(clientId);

        if (StringUtils.isNotEmpty(title)) {
            criteria.andOperator(criteria.orOperator(Criteria.where("code").regex(".*" + title + ".*", "i"),
                    Criteria.where("description").regex(".*" + title + ".*", "i")));
        }
        return mongotemplate.find(Query.query(criteria).with(Sort.by(Sort.Direction.DESC, "createdDate")).with(paging), SystemConfigEntity.class);
    }
}
