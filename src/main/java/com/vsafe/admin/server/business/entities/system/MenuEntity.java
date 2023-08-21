package com.vsafe.admin.server.business.entities.system;

import com.vsafe.admin.server.business.entities.BaseEntity;
import com.vsafe.admin.server.business.entities.operation.sub.MenuPermission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.List;

@Document(collection = "cat_menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuEntity extends BaseEntity {
    @Id
    private String id;
    @Field(name = "name")
    private String name;
    @Field(name = "level")
    private Integer level;
    @Field(name = "description")
    private String description;
    @Field(targetType = FieldType.OBJECT_ID, name = "parent_id")
    private String parentId;
    @Field(name = "url")
    private String url;
    @Field(name = "icon")
    private String icon;
    @Field(name = "path_id")
    private String pathId;
    @Field(name = "is_visible")
    private Boolean isVisible = false;
    @Field(name = "order")
    private Integer order;
    @Field(name = "code")
    private String code;

    @Field(name = "permission")
    private List<MenuPermission> permission;
}
