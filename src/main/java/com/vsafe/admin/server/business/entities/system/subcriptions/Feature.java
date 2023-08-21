package com.vsafe.admin.server.business.entities.system.subcriptions;

import com.vsafe.admin.server.business.entities.AuditTable;
import com.vsafe.admin.server.business.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Feature {
    @NotBlank
    @Size(max = 64)
    private String key;
    @NotBlank
    @Size(max = 128)
    private String name;
    private boolean visible;
    private boolean checked;
    private boolean canCustomName;
    private int order;
}
