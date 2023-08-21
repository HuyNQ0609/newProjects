package com.vsafe.admin.server.business.entities.operation;

import com.vsafe.admin.server.business.entities.BaseEntity;
import com.vsafe.admin.server.business.entities.operation.sub.SensorEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "cus_device_config")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceConfigEntity extends BaseEntity {
    @Id
    private String id;

    @Field(targetType = FieldType.OBJECT_ID, name = "location_id")
    private String locationId;

    @Field(targetType = FieldType.OBJECT_ID, name = "area_id")
    private String areaId;

    @Field(targetType = FieldType.OBJECT_ID, name = "customer_id")
    private String customerId;

    @Field(name = "gateway_imei")
    private String gatewayImei;

    @Field(name = "gateway_name")
    private String gatewayName;

    @Field(name = "is_deleted")
    private Integer isDeleted;

    @Field(name = "sensor_list")
    private List<SensorEntity> sensorList;

}
