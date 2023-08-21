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

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "cus_device_status")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceStatusEntity extends BaseEntity {
    @Id
    private String id;

    @Field(targetType = FieldType.OBJECT_ID, name = "marker_id")
    private String markerId;

    @Field(targetType = FieldType.OBJECT_ID, name = "device_id")
    private String deviceId;

    @Field(name = "response_time")
    private Date responseTime;

}
