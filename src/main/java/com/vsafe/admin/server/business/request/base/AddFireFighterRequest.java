package com.vsafe.admin.server.business.request.base;

import com.vsafe.admin.server.business.entities.base.Representative;
import com.vsafe.admin.server.business.request.BaseRequest;
import jdk.jfr.Name;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AddFireFighterRequest extends BaseRequest {

    @NotNull(message = "Tên đơn vị không được để trống")
    @Size(max = 200, message = "Tên đơn vị không được quá 200 ký tự")
    private String orgName;

    @NotNull(message = "Hotline liên lạc không được để trống")
    @Size(max = 20, message = "Hotline liên lạc không được quá 20 ký tự")
    private String hotline;

    @Size(max = 20, message = "Hotline liên lạc 2 không được quá 20 ký tự")
    private String hotlineBackup;

    @NotNull(message = "Địa chỉ đơn vị không được để trống")
    @Size(max = 500, message = "Địa chỉ không được quá 500 ký tự")
    private String address;

    @NotNull(message = "Tỉnh/Thành phố đơn vị không được để trống")
    private Long provinceId;

    private Long districtId;

    @NotNull(message = "Cấp đơn vị không được để trống")
    private Long level;
    private String parentId;

    private Representative representative;
    private Representative representativeBackup;
}
