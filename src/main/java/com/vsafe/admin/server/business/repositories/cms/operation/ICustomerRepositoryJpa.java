package com.vsafe.admin.server.business.repositories.cms.operation;

import com.vsafe.admin.server.business.entities.operation.CustomerEntity;
import com.vsafe.admin.server.business.repositories.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface ICustomerRepositoryJpa extends BaseRepository<CustomerEntity, String> {
    Optional<CustomerEntity> findByUserName(String userName);

}
