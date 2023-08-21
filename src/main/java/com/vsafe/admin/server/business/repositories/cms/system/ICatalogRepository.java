package com.vsafe.admin.server.business.repositories.cms.system;

import com.vsafe.admin.server.business.entities.system.CatalogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ICatalogRepository  extends MongoRepository<CatalogEntity, String> {
    List<CatalogEntity> findAllByCatalogType_Type(String type);

    boolean existsByNameAndCatalogType_TypeAndIdNot(String name, String type, String id);

    @Query(value = "{'$and': [" +
            "{'$or': [{'$expr': {'$eq':  [?0, null]}},{'name': {'$regex': '?0', '$options':  'i'}}]}, " +
            "{'$or': [{'$expr': {'$eq':  [?1, null]}}, {'parentId':  ?1}]}, " +
            "{'$or': [{'$expr': {'$eq':  [?2, null]}}, {'catalogType.type': { '$regex': '^?2$', '$options':  'i'}}]}, " +
            "{'$or': [{'$expr': {'$eq':  [?3, null]}}, {'catalogType.id': ?3}]}, " +
            "]}")
    List<CatalogEntity> getCatalogList(
            String name, String parentId, String catalogType, String catalogTypeId
    );

    @Query(value = "{'$and': [" +
            "{'$or': [{'$expr': {'$eq':  [?0, null]}},{'name': {'$regex': '?0', '$options':  'i'}}]}, " +
            "{'$or': [{'$expr': {'$eq':  [?1, null]}}, {'catalogType.id': ?1}]}, " +
            "]}")
    List<CatalogEntity> getCatalogList(
            String name, String catalogTypeId
    );

    Long countByCatalogType_Type(String type);

}
