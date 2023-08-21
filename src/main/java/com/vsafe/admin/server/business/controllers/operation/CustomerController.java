package com.vsafe.admin.server.business.controllers.operation;

import com.vsafe.admin.server.business.request.operation.CustomerDetailRequest;
import com.vsafe.admin.server.business.request.operation.SearchCustomerRequest;
import com.vsafe.admin.server.business.services.opearation.CustomerService;
import com.vsafe.admin.server.core.configurations.ResourcePath;
import com.vsafe.admin.server.helpers.utils.file.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;

@RestController
@Slf4j
@RequestMapping(ResourcePath.BASE + "/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/list")
    public ResponseEntity<?> search(@RequestBody SearchCustomerRequest request) {
        return new ResponseEntity<>(customerService.searchWithPaging(request), HttpStatus.OK);
    }

    @GetMapping("/list-by-parent/{id}")
    public ResponseEntity<?> listByParent(@PathVariable String id) {
        return new ResponseEntity<>(customerService.listByParent(id), HttpStatus.OK);
    }

    @PostMapping("/list-all")
    public ResponseEntity<?> searchWithoutPage(@RequestBody SearchCustomerRequest request) {
        return new ResponseEntity<>(customerService.searchWithoutPaging(request), HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getDetail(@RequestParam String id) {
        return new ResponseEntity<>(customerService.getDetail(id), HttpStatus.OK);
    }

    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody SearchCustomerRequest request) {
        try {
            ExcelUtils.export(response, customerService.export(request), "customer.xlsx");
        } catch (IOException e) {
            log.error("Có lỗi khi export danh sách khách hàng: " + e);
        }
    }

    @PostMapping("/save/info-detail")
    public ResponseEntity<?> updateInfo(@Valid @RequestParam("id") String id, @Valid @RequestParam("name") String name, @Valid @RequestParam("phoneNumber") String phoneNumber,
                                        @Valid @RequestParam("email") String email, @RequestParam(value = "provinceCode", required = false) String provinceCode, @RequestParam(value = "districtCode", required = false) String districtCode,
                                        @RequestParam(value = "wardCode", required = false) String wardCode, @RequestParam(value = "address", required = false) String address,
                                        @RequestParam(value = "customerType", required = false) Integer customerType,
                                        @RequestParam(value = "birthday", required = false) Date birthday,
                                        @RequestParam(value = "gender", required = false) Integer gender,
                                        @RequestParam(value = "identityNumber", required = false) String identityNumber,
                                        @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        CustomerDetailRequest request = new CustomerDetailRequest();
        request.setId(id);
        request.setName(name);
        request.setPhoneNumber(phoneNumber);
        request.setEmail(email);
        request.setProvinceCode(provinceCode);
        request.setDistrictCode(districtCode);
        request.setWardCode(wardCode);
        request.setAddress(address);
        request.setBirthday(birthday);
        request.setCustomerType(customerType);
        request.setGender(gender);
        request.setIdentityNumber(StringUtils.isNotEmpty(identityNumber) ? identityNumber.trim() : null);
        request.setAvatar(avatar);
        return new ResponseEntity<>(customerService.updateInfo(request), HttpStatus.OK);
    }

    @PostMapping("/save/member-info")
    public ResponseEntity<?> updateMemberInfo(@RequestParam(value = "id", required = false) String id, @Valid @RequestParam("name") String name, @Valid @RequestParam("phoneNumber") String phoneNumber,
                                              @Valid @RequestParam("email") String email, @RequestParam(value = "provinceCode", required = false) String provinceCode, @RequestParam(value = "districtCode", required = false) String districtCode,
                                              @RequestParam(value = "wardCode", required = false) String wardCode, @RequestParam(value = "address", required = false) String address,
                                              @RequestParam(value = "birthday", required = false) Date birthday,
                                              @RequestParam(value = "role", required = false) String role,
                                              @RequestParam(value = "parentId", required = false) String parentId,
                                              @RequestParam(value = "gender", required = false) Integer gender,
                                              @RequestParam(value = "identityNumber", required = false) String identityNumber) {
        CustomerDetailRequest request = new CustomerDetailRequest();
        if (StringUtils.isNotEmpty(id))
            request.setId(id);
        request.setName(name);
        request.setPhoneNumber(phoneNumber);
        request.setEmail(email);
        request.setProvinceCode(provinceCode);
        request.setDistrictCode(districtCode);
        request.setWardCode(wardCode);
        request.setAddress(address);
        request.setRole(role);
        request.setBirthday(birthday);
        request.setParentId(parentId);
        request.setGender(gender);
        request.setIdentityNumber(StringUtils.isNotEmpty(identityNumber) ? identityNumber.trim() : null);
        if (StringUtils.isNotEmpty(id))
            return new ResponseEntity<>(customerService.updateMemberInfo(request), HttpStatus.OK);
        return new ResponseEntity<>(customerService.addMemberInfo(request), HttpStatus.OK);
    }

    @PostMapping("/save/info-status")
    public ResponseEntity<?> saveInfoStatus(@RequestBody CustomerDetailRequest request) {
        return new ResponseEntity<>(customerService.saveInfoStatus(request), HttpStatus.OK);
    }

    @PostMapping("/save/two-factor")
    public ResponseEntity<?> saveTwoFactor(@RequestBody CustomerDetailRequest request) {
        return new ResponseEntity<>(customerService.saveTwoFactor(request), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCustomer(@Valid @RequestParam("name") String name, @Valid @RequestParam("phoneNumber") String phoneNumber,
                                         @Valid @RequestParam("email") String email, @RequestParam(value = "provinceCode", required = false) String provinceCode, @RequestParam(value = "districtCode", required = false) String districtCode,
                                         @RequestParam(value = "wardCode", required = false) String wardCode, @RequestParam(value = "address", required = false) String address,
                                         @RequestParam(value = "customerType", required = false) Integer customerType,
                                         @RequestParam(value = "birthday", required = false) Date birthday,
                                         @RequestParam(value = "gender", required = false) Integer gender,
                                         @RequestParam(value = "identityNumber", required = false) String identityNumber,
                                         @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        CustomerDetailRequest request = new CustomerDetailRequest();
        request.setName(name.trim());
        request.setPhoneNumber(phoneNumber.trim());
        request.setEmail(email.trim());
        request.setBirthday(birthday);
        request.setCustomerType(customerType);
        request.setGender(gender);
        request.setProvinceCode(StringUtils.isNotEmpty(provinceCode) ? provinceCode.trim() : null);
        request.setDistrictCode(StringUtils.isNotEmpty(districtCode) ? districtCode.trim() : null);
        request.setWardCode(StringUtils.isNotEmpty(wardCode) ? wardCode.trim() : null);
        request.setAddress(StringUtils.isNotEmpty(address) ? address.trim() : null);
        request.setIdentityNumber(StringUtils.isNotEmpty(identityNumber) ? identityNumber.trim() : null);
        request.setAvatar(avatar);
        return new ResponseEntity<>(customerService.addCustomer(request), HttpStatus.OK);
    }
}
