package com.vsafe.admin.server.business.entities.system;

import com.vsafe.admin.server.business.entities.AuditTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;


/**
 * Danh mục đơn vị hành chính
 */
@Document(collection = "cat_administrative_unit")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministrativeUnitEntity extends AuditTable {
    @Id
    private String code;

    private String name;

    private Integer type;

    private String path;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdministrativeUnitEntity that = (AdministrativeUnitEntity) o;
        return Objects.equals(code, that.code) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, type);
    }
}
