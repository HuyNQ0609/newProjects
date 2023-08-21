package com.vsafe.admin.server.business.services.system;

import com.vsafe.admin.server.business.request.system.ChangePasswordRequest;
import com.vsafe.admin.server.business.request.system.CreateUserRequest;
import com.vsafe.admin.server.business.request.system.LoginRequest;
import com.vsafe.admin.server.business.response.BaseResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity login(LoginRequest request);

    BaseResponse logout(String token);

    ResponseEntity refreshToken(String refreshToken);

    BaseResponse changePassword(ChangePasswordRequest request);

    BaseResponse addUser(CreateUserRequest request);
    BaseResponse updateUserName(CreateUserRequest request);
}
