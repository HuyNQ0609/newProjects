package com.vsafe.admin.server.business.repositories.cms.base;

import com.vsafe.admin.server.business.entities.base.FireFighterEntity;
import com.vsafe.admin.server.business.request.base.SearchFireFighterRequest;

import java.util.List;

public interface IFireFighterRepositoryCustom {

    long countSearch(SearchFireFighterRequest request);

    List<FireFighterEntity> searchWithPaging(SearchFireFighterRequest request);

    List<FireFighterEntity> searchWithoutPaging(SearchFireFighterRequest request);

}
