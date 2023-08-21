package com.vsafe.admin.server.business.repositories.cms.system;

import com.vsafe.admin.server.business.entities.operation.CustomerEntity;
import com.vsafe.admin.server.business.entities.system.EmployeeEntity;
import com.vsafe.admin.server.business.request.operation.SearchCustomerRequest;
import com.vsafe.admin.server.business.request.system.SearchEmployeeRequest;
import com.vsafe.admin.server.helpers.constants.BusinessConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.vsafe.admin.server.helpers.constants.SystemConstants.DEFAULT_PAGE_NUMBER;
import static com.vsafe.admin.server.helpers.constants.SystemConstants.DEFAULT_PAGE_SIZE;

@Repository
public class EmployeeRepositoryCustomImpl implements IEmployeeRepositoryCustom {

    private final MongoTemplate mongotemplate;

    public EmployeeRepositoryCustomImpl(MongoTemplate mongotemplate) {
        this.mongotemplate = mongotemplate;
    }

    @Override
    public long countSearch(SearchEmployeeRequest request, String clientId) {
        String name = request.getName();
        Criteria criteria = Criteria.where("clientId").is(clientId);

        if (StringUtils.isNotEmpty(name)) {
            criteria.andOperator(criteria.orOperator(Criteria.where("name").regex(".*" + name + ".*", "i"),
                    Criteria.where("phoneNumber").regex(".*" + name + ".*", "i"),
                    Criteria.where("identityNumber").regex(".*" + name + ".*", "i"),
                    Criteria.where("email").regex(".*" + name + ".*", "i")));
        }
        return mongotemplate.count(Query.query(criteria), EmployeeEntity.class);
    }

    @Override
    public List<EmployeeEntity> searchWithPaging(SearchEmployeeRequest request, String clientId) {
        Pageable paging = PageRequest.of(request.getPageNumber() <= 0 ? DEFAULT_PAGE_NUMBER : request.getPageNumber() - 1, request.getPageSize() == 0 ? DEFAULT_PAGE_SIZE
                : request.getPageSize());
        String name = request.getName();
        Criteria criteria = Criteria.where("clientId").is(clientId);

        if (StringUtils.isNotEmpty(name)) {
            criteria.andOperator(criteria.orOperator(Criteria.where("name").regex(".*" + name + ".*", "i"),
                    Criteria.where("phoneNumber").regex(".*" + name + ".*", "i"),
                    Criteria.where("identityNumber").regex(".*" + name + ".*", "i"),
                    Criteria.where("email").regex(".*" + name + ".*", "i")));
        }
        return mongotemplate.find(Query.query(criteria).with(Sort.by(Sort.Direction.DESC, "createdDate")).with(paging), EmployeeEntity.class);
    }
}
