package com.vsafe.admin.server.business.entities.operation;

import com.vsafe.admin.server.business.entities.BaseEntity;
import com.vsafe.admin.server.business.entities.operation.sub.Position;
import com.vsafe.admin.server.business.entities.operation.sub.Representative;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "cus_area")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AreaEntity extends BaseEntity {
    @Id
    private String id;
    @Field(name = "address")
    private String address;
    @Field(name = "description")
    private String description;
    @Field(name = "district_code")
    private String districtCode;
    @Email
    private String email;
    @Field(targetType = FieldType.OBJECT_ID, name = "fire_fighter_id")
    private String fireFighterId;
    @Field(name = "marker_image")
    private String markerImage;
    @Size(min = 9, max = 20)
    @Field(name = "phone")
    private String phone;
    @Field(name = "position")
    private Position position;
    @Field(name = "province_code")
    private String provinceCode;
    @Field(name = "region")
    private String region;
    @Field(name = "representative")
    private Representative representative;
    @NotBlank
    @Field(name = "title")
    private String title;
    @Field(name = "ward_code")
    private String wardCode;
}
