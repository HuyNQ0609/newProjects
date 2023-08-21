package com.vsafe.admin.server.business.request.system;

import com.vsafe.admin.server.business.request.BaseRequest;
import com.vsafe.admin.server.helpers.enums.UserType;
import com.vsafe.admin.server.helpers.validators.ValueOfEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChangePasswordRequest extends BaseRequest {

    @NotBlank(message = "Tên đăng nhập không được trống")
    private String userName;

    @NotBlank(message = "Mật khẩu không được trống")
    private String password;

    @NotBlank(message = "Token không được trống")
    private String token;
}
