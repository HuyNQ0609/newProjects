package com.vsafe.admin.server.business.entities.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Document(collection = "sys_client")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientEntity {
    @Id
    private String id;

    @NotNull
    @Size(min = 4, max = 255)
    @Indexed(unique = true)
    private String name;

    @Field(name = "wards_id")
    private Long wardId;
    @Field(name = "district_id")
    private Long districtId;
    @Field(name = "province_id")
    private Long provinceId;

    private String address;
    private String representative;

    @Field(name = "bank_account")
    private String bankAccount;

    @Field(name = "bank_name")
    private String bankName;

    @Field(name = "bank_branch")
    private String bankBranch;

    @Field(name = "phone_number")
    private String phoneNumber;

    private Integer status;

}
