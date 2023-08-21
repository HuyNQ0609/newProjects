package com.vsafe.admin.server.business.entities.operation.sub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorEntity {
    @Field(name = "imei")
    private String imei;
    @Field(name = "name")
    private String name;
    @Field(name = "status")
    private Integer status;
    @Field(name = "type")
    private String type;
    @Field(name = "is_deleted")
    private Integer isDeleted = 0;
}
