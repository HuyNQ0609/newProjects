package com.vsafe.admin.server.business.controllers.system;

import com.vsafe.admin.server.business.request.operation.CustomerDetailRequest;
import com.vsafe.admin.server.business.request.system.EmployeeDetailRequest;
import com.vsafe.admin.server.business.request.system.SearchEmployeeRequest;
import com.vsafe.admin.server.business.services.system.EmployeeService;
import com.vsafe.admin.server.core.configurations.ResourcePath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Date;

@RestController
@Slf4j
@RequestMapping(ResourcePath.BASE + "/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/all")
    public ResponseEntity getAll(@RequestBody SearchEmployeeRequest searchEmployeeRequest) {
        return new ResponseEntity<>(employeeService.searchWithPaging(searchEmployeeRequest), HttpStatus.OK);
    }

    @PostMapping("/save/info")
    public ResponseEntity<?> updateInfo(@Valid @RequestParam("id") String id, @Valid @RequestParam("fullName") String fullName, @Valid @RequestParam("phoneNumber") String phoneNumber,
                                        @Valid @RequestParam("email") String email, @RequestParam(value = "provinceCode", required = false) String provinceCode, @RequestParam(value = "districtCode", required = false) String districtCode,
                                        @RequestParam(value = "wardCode", required = false) String wardCode, @RequestParam(value = "address", required = false) String address,
                                        @RequestParam(value = "birthday", required = false) Date birthday,
                                        @RequestParam(value = "gender", required = false) Integer gender,
                                        @RequestParam(value = "roles", required = false) String[] roles,
                                        @RequestParam(value = "identityNumber", required = false) String identityNumber,
                                        @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        EmployeeDetailRequest request = new EmployeeDetailRequest();
        request.setId(id);
        request.setFullName(fullName);
        request.setEmail(email);
        request.setProvinceCode(provinceCode);
        request.setDistrictCode(districtCode);
        request.setWardCode(wardCode);
        request.setAddress(address);
        request.setBirthday(birthday);
        request.setGender(gender);
        request.setIdentityNumber(StringUtils.isNotEmpty(identityNumber) ? identityNumber.trim() : null);
        request.setAvatar(avatar);
        request.setRoles(roles);
        return new ResponseEntity<>(employeeService.updateInfo(request), HttpStatus.OK);
    }

    @PostMapping("/save/new")
    public ResponseEntity<?> updateInfo(@Valid @RequestParam("fullName") String fullName, @Valid @RequestParam("phoneNumber") String phoneNumber,
                                        @Valid @RequestParam("email") String email, @RequestParam(value = "provinceCode", required = false) String provinceCode, @RequestParam(value = "districtCode", required = false) String districtCode,
                                        @RequestParam(value = "wardCode", required = false) String wardCode, @RequestParam(value = "address", required = false) String address,
                                        @RequestParam(value = "birthday", required = false) Date birthday,
                                        @RequestParam(value = "gender", required = false) Integer gender,
                                        @RequestParam(value = "roles", required = false) String[] roles,
                                        @RequestParam(value = "identityNumber", required = false) String identityNumber,
                                        @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        EmployeeDetailRequest request = new EmployeeDetailRequest();
        request.setFullName(fullName);
        request.setPhoneNumber(phoneNumber);
        request.setEmail(email);
        request.setProvinceCode(provinceCode);
        request.setDistrictCode(districtCode);
        request.setWardCode(wardCode);
        request.setAddress(address);
        request.setBirthday(birthday);
        request.setGender(gender);
        request.setIdentityNumber(StringUtils.isNotEmpty(identityNumber) ? identityNumber.trim() : null);
        request.setAvatar(avatar);
        request.setRoles(roles);
        return new ResponseEntity<>(employeeService.insertInfo(request), HttpStatus.OK);
    }

    @PostMapping("/save/info-status")
    public ResponseEntity<?> saveInfoStatus(@RequestBody EmployeeDetailRequest request) {
        return new ResponseEntity<>(employeeService.saveInfoStatus(request), HttpStatus.OK);
    }
}
