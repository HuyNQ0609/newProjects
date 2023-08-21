package com.vsafe.admin.server.business.entities.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "cat_employee_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRoleEntity {

    @Id
    private String id;

    @Field(targetType = FieldType.OBJECT_ID, name = "employee_id")
    private String employeeId;

    @Field(targetType = FieldType.OBJECT_ID, name = "client_id")
    private String clientId;

    @Field(targetType = FieldType.OBJECT_ID, name = "role_id")
    private String roleId;

}
