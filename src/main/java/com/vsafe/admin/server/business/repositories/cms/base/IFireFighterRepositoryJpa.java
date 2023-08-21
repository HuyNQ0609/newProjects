package com.vsafe.admin.server.business.repositories.cms.base;

import com.vsafe.admin.server.business.entities.base.FireFighterEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFireFighterRepositoryJpa extends MongoRepository<FireFighterEntity, String> {

    List<FireFighterEntity> findByLevel(Long level, Pageable pageable);

    List<FireFighterEntity> findByLevelAndParentId(Long level, String parentId);
}