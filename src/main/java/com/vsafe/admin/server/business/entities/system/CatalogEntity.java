package com.vsafe.admin.server.business.entities.system;

import com.vsafe.admin.server.business.entities.AuditTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "cat_catalog")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogEntity extends AuditTable {
    @Id
    private String id;

    private String name;

    private Boolean enable = true;

    private Integer priority = 0;

    @Field(targetType = FieldType.OBJECT_ID, name = "parent_id")
    private String parentId;

    @Field(name = "catalog_type")
    private CatalogTypeEntity catalogType;

    private String description;

}
