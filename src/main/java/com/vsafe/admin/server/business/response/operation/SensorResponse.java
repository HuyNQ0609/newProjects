package com.vsafe.admin.server.business.response.operation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorResponse {

    private String imei;
    private String name;
    private Integer status;
    private String type;
    private String location;
    private String area;

}
