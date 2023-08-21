package com.vsafe.admin.server.business.response.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vsafe.admin.server.business.entities.system.CatalogEntity;
import com.vsafe.admin.server.business.entities.system.CatalogTypeEntity;
import com.vsafe.admin.server.helpers.utils.datastructure.tree.Node;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogResponse implements Serializable, Node<CatalogResponse> {
    private String id;

    private String name;

    private Boolean enable;

    private CatalogTypeEntity catalogType;
    private String description;

    private Integer priority = 0;
    private String parentId;
    private List<CatalogResponse> children;
    @JsonIgnore
    private transient CatalogResponse parent;

    public static CatalogResponse of(CatalogEntity entity) {
        CatalogResponse response = new CatalogResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

}
