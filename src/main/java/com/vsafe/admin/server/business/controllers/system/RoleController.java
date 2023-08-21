package com.vsafe.admin.server.business.controllers.system;

import com.vsafe.admin.server.business.request.system.SearchRoleRequest;
import com.vsafe.admin.server.business.request.system.UserByRoleRequest;
import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.business.services.system.RoleService;
import com.vsafe.admin.server.core.configurations.ResourcePath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(ResourcePath.BASE + "/role")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/all")
    public ResponseEntity getAll() {
        return new ResponseEntity<>(roleService.getAllActive(), HttpStatus.OK);
    }

    @PostMapping("/search")
    @PreAuthorize("hasPermission('ROLE_MANAGEMENT','VIEW')")
    public ResponseEntity searchRole(@RequestBody SearchRoleRequest request) {
        return new ResponseEntity<>(roleService.searchRole(request), HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasPermission('ROLE_MANAGEMENT','ADD')")
    public ResponseEntity addRole(@RequestBody SearchRoleRequest request) {
        return new ResponseEntity<>(roleService.addRole(request), HttpStatus.OK);
    }

    @PostMapping("/update")
    @PreAuthorize("hasPermission('ROLE_MANAGEMENT','UPDATE')")
    public ResponseEntity updateRole(@RequestBody SearchRoleRequest request) {
        if (!StringUtils.isNotEmpty(request.getId()))
            return new ResponseEntity<>(BaseResponse.error("Thông tin cập nhật không hợp lệ"), HttpStatus.OK);
        return new ResponseEntity<>(roleService.updateRole(request), HttpStatus.OK);
    }

    @PostMapping("/state")
    @PreAuthorize("hasPermission('ROLE_MANAGEMENT','UPDATE')")
    public ResponseEntity updateStatusRole(@RequestBody SearchRoleRequest request) {
        if (!StringUtils.isNotEmpty(request.getId()))
            return new ResponseEntity<>(BaseResponse.error("Thông tin cập nhật không hợp lệ"), HttpStatus.OK);
        return new ResponseEntity<>(roleService.updateStatusRole(request), HttpStatus.OK);
    }

    @PostMapping("/remove-user-role")
    @PreAuthorize("hasPermission('ROLE_MANAGEMENT','DELETE')")
    public ResponseEntity removeUserRole(@RequestBody UserByRoleRequest request) {
        if (!StringUtils.isNotEmpty(request.getRoleId()) || (request.getUserIds() == null || request.getUserIds().size() == 0))
            return new ResponseEntity<>(BaseResponse.error("Thông tin không hợp lệ"), HttpStatus.OK);
        return new ResponseEntity<>(roleService.removeUserRole(request.getUserIds(), request.getRoleId()), HttpStatus.OK);
    }

}
