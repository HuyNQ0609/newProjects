package com.vsafe.admin.server.business.request.system;

import com.vsafe.admin.server.business.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AddMenuRequest extends BaseRequest {

    @NotNull(message = "Tên chức năng không được để trống")
    @Size(max = 20, message = "Tên chức năng không được quá 20 ký tự")
    private String name;
    private int level;
    @Size(max = 2000, message = "Mô tả không được quá 2000 ký tự")
    private String description;
    private String parentId;
    private String url;
    private String icon;
    private boolean isVisible = false;
}
