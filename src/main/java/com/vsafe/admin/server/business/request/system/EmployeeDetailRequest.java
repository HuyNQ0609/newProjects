package com.vsafe.admin.server.business.request.system;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vsafe.admin.server.business.request.BaseRequest;
import com.vsafe.admin.server.helpers.utils.DateDeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetailRequest extends BaseRequest {
    private String id;
    @NotBlank(message = "Tên Nhân viên không được trống")
    private String fullName;
    @NotBlank(message = "Số điện thoại không được trống")
    private String phoneNumber;
    @NotBlank(message = "Email không được trống")
    private String email;
    private String provinceCode;
    private String districtCode;
    private String wardCode;
    private String address;
    private MultipartFile avatar;
    private String identityNumber;
    private Integer gender;
    @JsonDeserialize(using = DateDeSerializer.class)
    private Date birthday;
    private Integer status;
    private String[] roles;

    public EmployeeDetailRequest(String id, String identityNumber, Integer gender, Date birthday) {
        this.id = id;
        this.identityNumber = identityNumber;
        this.gender = gender;
        this.birthday = birthday;
    }

    public Integer getStatus() {
        return Objects.nonNull(status) ? status : 1;
    }
}
