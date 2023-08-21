package com.vsafe.admin.server.business.repositories.cms.system;

import com.vsafe.admin.server.business.entities.system.EmployeeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IEmployeeRepositoryJpa extends MongoRepository<EmployeeEntity, String> {
    Optional<EmployeeEntity> findByUserName(String userName);
    EmployeeEntity findFirstById(String id);

    List<EmployeeEntity> findByIdIn(List<String> ids);
    List<EmployeeEntity> findByPhoneNumberAndClientId(String phoneNumber, String clientId);
    List<EmployeeEntity> findByEmailAndClientId(String email, String clientId);
    List<EmployeeEntity> findByIdentityNumberAndClientId(String identityNumber, String clientId);
}
