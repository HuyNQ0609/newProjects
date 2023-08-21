package com.vsafe.admin.server.business.entities.base;

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

    @Field(name = "phone")
    private String phone;

    @Field(name = "mail")
    private String mail;
}
