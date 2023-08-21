package com.vsafe.admin.server.business.entities.operation.sub;

import com.vsafe.admin.server.business.entities.BaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
public class SetUserRoleEntity extends BaseEntity {
    
    @Field(targetType = FieldType.OBJECT_ID, name = "role_id")
    private String roleId;
}
