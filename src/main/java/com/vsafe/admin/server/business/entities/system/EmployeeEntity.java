package com.vsafe.admin.server.business.entities.system;

import com.vsafe.admin.server.business.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.Date;
import java.util.List;

@Document(collection = "cus_employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeEntity extends BaseEntity {
    @Id
    private String id;
    @Field(name = "full_name")
    private String fullName;
    @Field(name = "phone_number")
    private String phoneNumber;
    @Field(name = "identity_number")
    private String identityNumber;
    @Field(name = "gender")
    private Integer gender;
    @Field(name = "birthday")
    private Date birthday;
    @Field(name = "user_name")
    private String userName;
    @Field(name = "email")
    private String email;
    @Field(name = "district_code")
    private String districtCode;
    @Field(name = "province_code")
    private String provinceCode;
    @Field(name = "ward_code")
    private String wardCode;
    @Field(name = "address")
    private String address;
    @Field(name = "avatar")
    private String avatar;

    @Transient
    private List<String> roles;

    @Transient
    private List<RoleEntity> roleModels;
}
