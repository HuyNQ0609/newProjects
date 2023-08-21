package com.vsafe.admin.server.business.request.system;

import com.vsafe.admin.server.business.request.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministrativeUnitFilter extends PageRequest {
    Integer type;

    String name;

    String parentCode;

    String code;
}
