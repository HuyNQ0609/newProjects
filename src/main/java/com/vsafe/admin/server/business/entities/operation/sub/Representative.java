package com.vsafe.admin.server.business.entities.operation.sub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Representative {

    @Field(name = "name")
    private String name;

    @Field(name = "position")
    private String position;

    @Field(name = "phone_number")
    private String phoneNumber;

    @Field(name = "email")
    private String email;

    @Field(name = "identity_number")
    private String identityNumber;
}
