package com.vsafe.admin.server.business.repositories.cms.system;

import com.vsafe.admin.server.business.entities.system.AdministrativeUnitEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAdministrativeUnitRepositoryJpa extends MongoRepository<AdministrativeUnitEntity, String> {
    @Query(value = "{'$or': [{'$where': '?0 == null'}, {'path':  {'$regex': ',?0,$', '$options':  'i'}}]}")
    List<AdministrativeUnitEntity> getChildrens(String code);

    @Query(value = "{'$and': [" +
            "{'$or': [{'$expr': {'$eq':  [?1, null]}},{'name': {'$regex': '?1', '$options':  'i'}}]}, " +
            "{'$or': [{'$expr': {'$eq':  [?0, null]}}, {'type':  ?0}]}, " +
            "{'$or': [{'$expr': {'$eq':  [?2, null]}}, {'code':  ?2}]}, " +
            "{'$or': [{'$expr': {'$eq':  [?3, null]}},{ 'path' : {'$regex': ',?3,$', '$options':  'i'}}]}" +
            "]}")
    Page<AdministrativeUnitEntity> getListByType(Integer type, String name, String code, String parentCode, Pageable pageable);
    Optional<AdministrativeUnitEntity> findByCodeAndType(String code, int type);
    Optional<AdministrativeUnitEntity> findByCode(String code);

    AdministrativeUnitEntity findFirstByCode(String code);
    List<AdministrativeUnitEntity> findByCodeIn(String[] code);

    @Query(value = "{'path':  {'$regex': '^,?0,$', '$options':  'i'}}", count = true)
    Long countDistrict(String code);

    @Query(value = "{'$or':[{'path':  {'$regex': '^,[0-9]*,?0,$', '$options':  'i'}}, {'path':  {'$regex': '^,?0,[0-9]*,$', '$options':  'i'}}]}", count = true)
    Long countWard(String code);

    @Query(value = "{'$or':[" +
            "{'path':  {'$regex': '^,?0,[0-9]*,[0-9]*,$', '$options':  'i'}}," +
            "{'path':  {'$regex': '^,[0-9]*,?0,[0-9]*,$', '$options':  'i'}}," +
            "{'path':  {'$regex': '^,[0-9]*,[0-9]*,?0,$', '$options':  'i'}}" +
            "]}", count = true)
    Long countVillage(String code);
}
