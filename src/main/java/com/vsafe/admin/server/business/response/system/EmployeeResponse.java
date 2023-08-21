package com.vsafe.admin.server.business.response.system;

import com.vsafe.admin.server.business.entities.system.EmployeeEntity;
import com.vsafe.admin.server.helpers.utils.mapper.MapperFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private String id;
    private String fullName;
    private Integer phoneNumber;
    private String identityNumber;
    private Integer gender;
    private Date birthday;
    private String email;
    private String clientId;

    public static EmployeeResponse of(EmployeeEntity employee) {
        ModelMapper mapper = MapperFactory.mapper(MapperFactory.ModelMapperOptions.DEEP_COPY);
        return mapper.map(employee, EmployeeResponse.class);
    }
}
