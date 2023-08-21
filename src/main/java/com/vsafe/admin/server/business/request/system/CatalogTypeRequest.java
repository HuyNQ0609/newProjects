package com.vsafe.admin.server.business.request.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogTypeRequest {
    private String id;
    @NotBlank(message = "Tên loại Catalog không được trống")
    @Size(min=1, max = 50, message = "Tên loại Catalog tối đa 50 ký tự")
    private String name;

    @Pattern(regexp = "^[A-Z_]*$", message = "Loại Catalog chỉ chấp nhận viết hoa và dấu _")
    @Size(min=1, max = 100, message = "Loại Catalog tối đa 100 ký tự")
    private String type;

    @Size(max = 100, message = "Loại Catalog tối đa 100 ký tự")
    private String description;

    private Boolean enable = true;
}
