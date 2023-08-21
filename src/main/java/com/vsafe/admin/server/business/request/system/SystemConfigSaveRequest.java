package com.vsafe.admin.server.business.request.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfigSaveRequest {

    private String id;

    @NotBlank(message = "Mã code không được trống")
    private String code;
    @NotBlank(message = "Giá trị value không được trống")
    private String value;

    private String description;

    @Range(min = 0, max = 2, message = "Trạng thái không hợp lệ")
    private Integer status;

    public Integer getStatus() {
        return Objects.nonNull(status) ? status : 1;
    }
}
