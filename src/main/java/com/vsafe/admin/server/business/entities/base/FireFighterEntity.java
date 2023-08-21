package com.vsafe.admin.server.business.entities.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "sys_fire_fighter")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FireFighterEntity {
    @Id
    private String id;

    @Field(name = "org_name")
    private String orgName;

    @Field(name = "hotline")
    private String hotline;

    @Field(name = "hotline_backup")
    private String hotlineBackup;

    @Field(name = "level")
    private Long level;

    @Field(name = "address")
    private String address;

    @Field(name = "province_id")
    private Long provinceId;

    @Field(name = "district_id")
    private Long districtId;

    @Field(targetType = FieldType.OBJECT_ID, name = "parent_id")
    private String parentId;

    @Field(name = "representative")
    private Representative representative;
    @Field(name = "representative_backup")
    private Representative representativeBackup;

}

