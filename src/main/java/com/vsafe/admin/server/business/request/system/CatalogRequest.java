package com.vsafe.admin.server.business.request.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogRequest {
    private String id;

    @NotBlank(message = "Tên Catalog không dược trống")
    @Size(min=1, max = 500, message = "Tên Catalog tối đa 500 ký tự")
    private String name;
    private Boolean enable = true;

    private String parentId;

    private Integer priority = 0;

    @NotBlank(message = "Loại Catalog không dược trống")
    @Pattern(regexp = "^[A-Z_]*$", message = "Loại Catalog chỉ chấp nhận viết hoa và dấu _")
    private String catalogType;
    private String description;

}
