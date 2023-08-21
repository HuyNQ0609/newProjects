package com.vsafe.admin.server.business.entities.operation;

import com.vsafe.admin.server.business.entities.BaseEntity;
import com.vsafe.admin.server.business.entities.system.AdministrativeUnitEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "cus_customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity extends BaseEntity {
    @Id
    private String id;

    @NotNull
    @Size(min = 4, max = 255)
    private String name;

    @Size(min = 9, max = 20)
    @Field(name = "phone_number")
    private String phoneNumber;

    @Email
    private String email;

    private String address;

    @Size(min = 9, max = 20)
    @Field(name = "identity_number")
    private String identityNumber;

    private Integer gender;

    @Field(name = "birthday")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthday;

    @Field(name = "verification_type")
    private String verificationType;

    @Field(name = "avatar")
    private String avatar;

    @Field(name = "district_code")
    private String districtCode;

    @Field(name = "province_code")
    private String provinceCode;

    @Field(name = "user_name")
    private String userName;

    @Field(name = "ward_code")
    private String wardCode;

    @Field(name = "customer_type")
    private Integer customerType;

    @Field(name = "tax_code")
    private String taxCode;

    @Field(name = "last_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;

    @Field(name = "is_acc_main")
    private Integer isAccMain;

    @Field(name = "role")
    private String role;

    @Field(targetType = FieldType.OBJECT_ID, name = "parent_id")
    private String parentId;

    @Transient
    private AdministrativeUnitEntity province;

    @Transient
    private AdministrativeUnitEntity ward;

    @Transient
    private AdministrativeUnitEntity district;

    @Field(name = "quantity_area")
    private Integer quantityArea = 0;

    @Field(name = "quantity_device")
    private Integer quantityDevice = 0;

}
