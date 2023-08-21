package com.vsafe.admin.server.business.repositories.cms.system;

import com.vsafe.admin.server.business.entities.system.CatalogTypeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ICatalogTypeRepository extends MongoRepository<CatalogTypeEntity, String> {
    boolean existsByIdNotAndType(String id, String type);
    boolean existsByIdNotAndName(String id, String name);
    boolean existsByType( String type);
    Optional<CatalogTypeEntity> findByType(String type);
}
