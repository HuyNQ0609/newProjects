package com.vsafe.admin.server.business.entities.operation.sub;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.List;

@Data
public class MenuPermission {

    @Field(targetType = FieldType.OBJECT_ID, name = "role_id")
    private String roleId;

    @Field(name = "actions")
    private List<String> actions;
}
