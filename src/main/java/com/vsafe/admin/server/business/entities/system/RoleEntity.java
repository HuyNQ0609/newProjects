package com.vsafe.admin.server.business.entities.system;

import com.vsafe.admin.server.business.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "cat_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity extends BaseEntity {
    @Id
    private String id;
    @Field(name = "name")
    private String name;
    @Field(name = "description")
    private String description;

    @Transient
    private List<EmployeeEntity> employeeList;
}
