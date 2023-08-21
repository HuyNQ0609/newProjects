package com.vsafe.admin.server.business.request.base;

import com.vsafe.admin.server.business.entities.base.Representative;
import com.vsafe.admin.server.business.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdateFireFighterRequest extends BaseRequest {

    @NotBlank(message = "Mã đơn vị không được để trống")
    private String id;

    @Size(max = 200, message = "Tên đơn vị không được quá 200 ký tự")
    private String orgName;

    @Size(max = 20, message = "Hotline liên lạc không được quá 20 ký tự")
    private String hotline;

    @Size(max = 20, message = "Hotline liên lạc 2 không được quá 20 ký tự")
    private String hotlineBackup;

    @Size(max = 500, message = "Địa chỉ không được quá 500 ký tự")
    private String address;

    private Long provinceId;
    private Long districtId;
    private Long level;
    private String parentId;
    private Representative representative;
    private Representative representativeBackup;
}
