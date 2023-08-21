package com.vsafe.admin.server.business.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity extends AuditTable{

    @Field(targetType = FieldType.OBJECT_ID, name = "client_id")
    private String clientId;

    @Field(name = "status")
    private Integer status;

}
