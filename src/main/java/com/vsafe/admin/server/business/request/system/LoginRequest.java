package com.vsafe.admin.server.business.request.system;

import com.vsafe.admin.server.business.request.BaseRequest;
import com.vsafe.admin.server.helpers.enums.UserType;
import com.vsafe.admin.server.helpers.validators.ValueOfEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginRequest extends BaseRequest {

    @NotNull(message = "Tên đăng nhập không được trống")
    private String userName;

    @NotNull(message = "Mật khẩu không được trống")
    private String password;

    private String verifiedCode;

    @ValueOfEnum(enumClass = UserType.class, message = "Loại User không hợp lệ")
    private int userType = UserType.CUSTOMER.getValue();
}
