package com.vsafe.admin.server.business.response.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vsafe.admin.server.business.response.operation.CustomerResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailResponse implements Serializable {
    private String userName;
    private String id;
    private Integer status;
    private Integer userType;

    private Date lastLogin;

    private EmployeeResponse employeeInfo;

    private CustomerResponse customerInfo;

}
