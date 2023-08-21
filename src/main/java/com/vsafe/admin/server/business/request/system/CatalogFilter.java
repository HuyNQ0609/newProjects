package com.vsafe.admin.server.business.request.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogFilter {
    private String name;

    private String catalogTypeId;

    private String catalogType;
    private String parentId;
}
