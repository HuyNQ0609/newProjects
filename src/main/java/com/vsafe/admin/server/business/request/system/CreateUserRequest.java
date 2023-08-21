package com.vsafe.admin.server.business.request.system;

import com.vsafe.admin.server.business.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateUserRequest extends BaseRequest {

    @NotBlank(message = "Tên đăng nhập không được trống")
    private String userName;

    private String userNameOld;

    @NotBlank(message = "Mật khẩu không được trống")
    private String password;

    @NotNull(message = "Loại tài khoản không được trống")
    private Integer userType;

    @NotNull(message = "Trạng thái không được trống")
    private Integer status;

/*    @NotBlank(message = "Id khách hàng không được trống")
    private String customerId;*/

    @NotBlank(message = "Token không được trống")
    private String token;
}
