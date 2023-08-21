package com.vsafe.admin.server.business.request.operation;

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
public class CustomerDetailRequest extends BaseRequest {
    private String id;
    //Thông tin chi tiết
    @NotBlank(message = "Tên Khách hàng không được trống")
    private String name;
    @NotBlank(message = "Số điện thoại không được trống")
    private String phoneNumber;
    @NotBlank(message = "Email không được trống")
    private String email;
    private String provinceCode;
    private String districtCode;
    private String wardCode;
    private String address;
    private MultipartFile avatar;

    private String verificationType;

    //Thông tin định danh
    private String identityNumber;
    private Integer gender;
    @JsonDeserialize(using = DateDeSerializer.class)
    private Date birthday;

    //Khách hàng doanh nghiệp
    private String taxCode;
    private String businessCode;

    //Hủy kích hoạt tài khoản
    private Integer status;
    private Integer customerType;
    private String role;
    private String parentId;

    public CustomerDetailRequest(String id, String identityNumber, Integer gender, Date birthday) {
        this.id = id;
        this.identityNumber = identityNumber;
        this.gender = gender;
        this.birthday = birthday;
    }

    public Integer getCustomerType() {
        return Objects.nonNull(customerType) ? customerType : 0;
    }

    public Integer getStatus() {
        return Objects.nonNull(status) ? status : 1;
    }
}
