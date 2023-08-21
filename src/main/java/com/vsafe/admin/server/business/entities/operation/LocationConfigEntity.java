package com.vsafe.admin.server.business.entities.operation;

import com.vsafe.admin.server.business.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "cus_location_config")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationConfigEntity extends BaseEntity {

    @Id
    private String id;

    @Field(name = "code")
    private String code;
    @Field(name = "name")
    private String name;

    @Field(targetType = FieldType.OBJECT_ID, name = "area_id")
    private String areaId;
    @Field(name = "area_name")
    private String areaName;

    @Field(targetType = FieldType.OBJECT_ID, name = "customer_id")
    private String customerId;

    @Field(name = "is_deleted")
    private Integer isDeleted;
}
