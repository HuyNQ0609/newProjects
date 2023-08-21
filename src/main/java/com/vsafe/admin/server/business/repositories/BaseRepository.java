package com.vsafe.admin.server.business.repositories;

import com.vsafe.admin.server.business.entities.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID extends Serializable>
        extends MongoRepository<T, ID> {
    T findFirstByIdAndClientId(String id, String clientId);

    List<T> findAllByClientId(String clientId);

    List<T> findAllByClientId(String clientId, Pageable pageable);

    List<T> findByClientIdAndStatus(String clientId, Integer status);

    Page<T> findByClientIdAndStatus(String clientId, Integer status, Pageable pageable);
}


