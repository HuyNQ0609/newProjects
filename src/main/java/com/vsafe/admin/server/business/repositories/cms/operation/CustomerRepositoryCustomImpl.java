package com.vsafe.admin.server.business.repositories.cms.operation;

import com.vsafe.admin.server.business.entities.operation.CustomerEntity;
import com.vsafe.admin.server.business.request.operation.SearchCustomerRequest;
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
public class CustomerRepositoryCustomImpl implements ICustomerRepositoryCustom {

    private final MongoTemplate mongotemplate;

    public CustomerRepositoryCustomImpl(MongoTemplate mongotemplate) {
        this.mongotemplate = mongotemplate;
    }

    @Override
    public long countSearch(SearchCustomerRequest request, String clientId) {
        String name = request.getName();
        Criteria criteria = Criteria.where("clientId").is(clientId);

        criteria.and("isAccMain").is(BusinessConstant.CUSTOMER_ACCOUNT.MAIN_ACC);

        if (StringUtils.isNotEmpty(name)) {
            criteria.andOperator(criteria.orOperator(Criteria.where("name").regex(".*" + name + ".*", "i"),
                    Criteria.where("phoneNumber").regex(".*" + name + ".*", "i"),
                    Criteria.where("identityNumber").regex(".*" + name + ".*", "i"),
                    Criteria.where("email").regex(".*" + name + ".*", "i")));
        }
        if (Objects.nonNull(request.getCustomerType())) {
            criteria.and("customerType").is(request.getCustomerType());
        }
        if (Objects.nonNull(request.getIsExistsUser())) {
            if (request.getIsExistsUser().equals(0)) {
                criteria.and("userName").isNull();
            }
            if (request.getIsExistsUser().equals(1)) {
                criteria.and("userName").ne(null);
            }
        }
        long total = mongotemplate.count(Query.query(criteria), CustomerEntity.class);
        return total;
    }

    @Override
    public List<CustomerEntity> searchWithPaging(SearchCustomerRequest request, String clientId) {
        Pageable paging = PageRequest.of(request.getPageNumber() <= 0 ? DEFAULT_PAGE_NUMBER : request.getPageNumber() - 1, request.getPageSize() == 0 ? DEFAULT_PAGE_SIZE
                : request.getPageSize());
        String name = request.getName();
        Criteria criteria = Criteria.where("clientId").is(clientId);

        criteria.and("isAccMain").is(BusinessConstant.CUSTOMER_ACCOUNT.MAIN_ACC);

        if (StringUtils.isNotEmpty(name)) {
            criteria.andOperator(criteria.orOperator(Criteria.where("name").regex(".*" + name + ".*", "i"),
                    Criteria.where("phoneNumber").regex(".*" + name + ".*", "i"),
                    Criteria.where("identityNumber").regex(".*" + name + ".*", "i"),
                    Criteria.where("email").regex(".*" + name + ".*", "i")));
        }
        if (Objects.nonNull(request.getCustomerType())) {
            criteria.and("customerType").is(request.getCustomerType());
        }
        if (Objects.nonNull(request.getIsExistsUser())) {
            if (request.getIsExistsUser().equals(0)) {
                criteria.and("userName").isNull();
            }
            if (request.getIsExistsUser().equals(1)) {
                criteria.and("userName").ne(null);
            }
        }
        return mongotemplate.find(Query.query(criteria).with(Sort.by(Sort.Direction.DESC, "createdDate")).with(paging), CustomerEntity.class);
    }

    @Override
    public List<CustomerEntity> searchWithoutPaging(SearchCustomerRequest request, String clientId) {
        String name = request.getName();
        Criteria criteria = Criteria.where("clientId").is(clientId);

        criteria.and("isAccMain").is(BusinessConstant.CUSTOMER_ACCOUNT.MAIN_ACC);

        if (StringUtils.isNotEmpty(name)) {
            criteria.andOperator(criteria.orOperator(Criteria.where("name").regex(".*" + name + ".*", "i"),
                    Criteria.where("phoneNumber").regex(".*" + name + ".*", "i"),
                    Criteria.where("identityNumber").regex(".*" + name + ".*", "i"),
                    Criteria.where("email").regex(".*" + name + ".*", "i")));
        }
        if (Objects.nonNull(request.getCustomerType())) {
            criteria.and("customerType").is(request.getCustomerType());
        }
        if (Objects.nonNull(request.getIsExistsUser())) {
            if (request.getIsExistsUser().equals(0)) {
                criteria.and("userName").isNull();
            }
            if (request.getIsExistsUser().equals(1)) {
                criteria.and("userName").ne(null);
            }
        }
        return mongotemplate.find(Query.query(criteria).with(Sort.by(Sort.Direction.ASC, "createdDate")), CustomerEntity.class);
    }

    @Override
    public List<CustomerEntity> searchByPhoneNumber(String phoneNumber, String clientId) {
        Criteria criteria = Criteria.where("clientId").is(clientId);
        criteria.andOperator(Criteria.where("phoneNumber").is(phoneNumber));
        return mongotemplate.find(Query.query(criteria).with(Sort.by(Sort.Direction.ASC, "createdDate")), CustomerEntity.class);
    }

    @Override
    public List<CustomerEntity> searchByEmail(String email, String clientId) {
        Criteria criteria = Criteria.where("clientId").is(clientId);
        criteria.andOperator(Criteria.where("email").is(email));
        return mongotemplate.find(Query.query(criteria).with(Sort.by(Sort.Direction.ASC, "createdDate")), CustomerEntity.class);
    }

    @Override
    public List<CustomerEntity> searchByIdentityNumber(String identityNumber, String clientId) {
        Criteria criteria = Criteria.where("clientId").is(clientId);
        criteria.andOperator(Criteria.where("identityNumber").is(identityNumber));
        return mongotemplate.find(Query.query(criteria).with(Sort.by(Sort.Direction.ASC, "createdDate")), CustomerEntity.class);
    }

    @Override
    public List<CustomerEntity> findByParentIdAndClientId(String parentId, String clientId) {

        Criteria criteria = Criteria.where("clientId").is(clientId);
        criteria.and("parentId").is(parentId);

        return mongotemplate.find(Query.query(criteria).with(Sort.by(Sort.Direction.ASC, "createdDate")), CustomerEntity.class);
    }
}
