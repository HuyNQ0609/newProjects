package com.vsafe.admin.server.business.request.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministrativeUnitRequest implements Serializable {
    private String code;
    @NotBlank(message = "Tên Thôn/ Xóm không được trống")
    @Size(min=1, max = 200, message = "Tên đơn vị dài từ 6 kí tự đến 200 ký tự")
    private String name;

    @NotBlank(message = "Mã Phường/ Xã không được trống")
    private String wardCode;

}
