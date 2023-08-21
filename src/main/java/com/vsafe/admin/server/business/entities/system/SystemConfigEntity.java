package com.vsafe.admin.server.business.entities.system;

import com.vsafe.admin.server.business.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "cat_system_config")
@Data
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndex(def = "{'code': 1, 'client_id': 1}", unique = true)
public class SystemConfigEntity extends BaseEntity {
    @Id
    private String id;

    @NotNull
    @Field(name = "code")
    private String code;

    @NotNull
    @Field(name = "value")
    private String value;

    @Field(name = "description")
    private String description;

}
