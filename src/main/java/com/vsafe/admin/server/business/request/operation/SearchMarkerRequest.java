package com.vsafe.admin.server.business.request.operation;

import com.vsafe.admin.server.business.request.BaseRequest;
import com.vsafe.admin.server.helpers.constants.SystemConstants;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SearchMarkerRequest extends BaseRequest {

    private String provinceCode;
    private String districtCode;
    private String wardCode;
}
