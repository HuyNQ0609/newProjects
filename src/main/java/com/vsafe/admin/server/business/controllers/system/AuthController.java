package com.vsafe.admin.server.business.controllers.system;

import com.vsafe.admin.server.business.request.system.ChangePasswordRequest;
import com.vsafe.admin.server.business.request.system.CreateUserRequest;
import com.vsafe.admin.server.business.request.system.LoginRequest;
import com.vsafe.admin.server.business.services.system.AuthService;
import com.vsafe.admin.server.core.configurations.ResourcePath;
import com.vsafe.admin.server.helpers.enums.UserType;
import com.vsafe.admin.server.helpers.utils.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@Slf4j
@RequestMapping(ResourcePath.BASE + "/authentication")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        AssertUtils.defaultNotNull(authService);
        this.authService = authService;
    }

    @PostMapping("/cms-login")
    public ResponseEntity doLoginCms(@RequestBody @Valid LoginRequest request) {
        request.setUserType(UserType.INTERNAL.getValue());
        return authService.login(request);
    }

    @PostMapping("/login")
    public ResponseEntity doLogin(@RequestBody @Valid LoginRequest request) {
        request.setUserType(UserType.CUSTOMER.getValue());
        return authService.login(request);
    }

    @PostMapping("/logout")
    public ResponseEntity doLogout(@RequestBody String token) {
        return new ResponseEntity<>(authService.logout(token), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity doRefreshToken(@RequestBody String refreshToken) {
        return new ResponseEntity<>(authService.refreshToken(refreshToken), HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        return new ResponseEntity<>(authService.changePassword(request), HttpStatus.OK);
    }

    @PostMapping("/add-user")
    public ResponseEntity createUser(@RequestBody @Valid CreateUserRequest request) {
        return new ResponseEntity<>(authService.addUser(request), HttpStatus.OK);
    }
}
