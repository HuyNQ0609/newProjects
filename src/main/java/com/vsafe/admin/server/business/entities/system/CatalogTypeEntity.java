package com.vsafe.admin.server.business.entities.system;

import com.vsafe.admin.server.business.entities.AuditTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "cat_catalog_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogTypeEntity extends AuditTable {
    @Id
    private String id;

    @Indexed(unique = true)
    private String type;

    private Boolean enable = true;

    @Indexed(unique = true)
    private String name;

    private String description;
}
