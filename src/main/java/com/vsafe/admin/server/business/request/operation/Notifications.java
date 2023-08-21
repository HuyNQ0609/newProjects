package com.vsafe.admin.server.business.request.operation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notifications {

    @NotBlank(message = "MarkerId không được trống")
    private String markerId;

    @NotNull(message = "Trạng thái không được trống")
    @Range(min = 0, max = 2, message = "Trạng thái không hợp lệ")
    private Integer status;

    private String title;
    private Double lat;
    private Double lng;

    private String deviceId;
    private String codeEquipment;
    private String requestId;
    private String requestDate;
    private String secureCode;
}
