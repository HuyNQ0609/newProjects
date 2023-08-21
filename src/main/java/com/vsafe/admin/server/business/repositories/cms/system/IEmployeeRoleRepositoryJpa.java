package com.vsafe.admin.server.business.repositories.cms.system;

import com.vsafe.admin.server.business.entities.system.EmployeeRoleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEmployeeRoleRepositoryJpa extends MongoRepository<EmployeeRoleEntity, String> {

    List<EmployeeRoleEntity> findByEmployeeId(String userId);

    List<EmployeeRoleEntity> findByRoleId(String roleId);

    EmployeeRoleEntity findFirstByRoleIdAndEmployeeIdInAndClientId(String roleId, List<String> userId, String clientId);

}
