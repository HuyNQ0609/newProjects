package com.vsafe.admin.server.business.response.operation;

import com.vsafe.admin.server.business.entities.operation.CustomerEntity;
import com.vsafe.admin.server.core.annotations.ExcelColumn;
import com.vsafe.admin.server.helpers.utils.mapper.MapperFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {

    @ExcelColumn()
    private String id;

    @ExcelColumn()
    private String name;

    @ExcelColumn()
    private String phoneNumber;

    @Email
    @ExcelColumn()
    private String email;

    @ExcelColumn()
    private String address;

    @ExcelColumn()
    private String identityNumber;

    @ExcelColumn(isHidden = true)
    private Integer gender;

    @ExcelColumn(isHidden = true)
    private String clientId;

    @ExcelColumn()
    private String verificationType;


    public static CustomerResponse of (CustomerEntity customer) {
        ModelMapper mapper = MapperFactory.mapper(MapperFactory.ModelMapperOptions.DEEP_COPY);
        return mapper.map(customer, CustomerResponse.class);
    }

    public static List<CustomerResponse> of (List<CustomerEntity> customerEntities) {
        ModelMapper mapper = MapperFactory.mapper(MapperFactory.ModelMapperOptions.DEEP_COPY);
        return customerEntities.stream().map(customerEntity -> mapper.map(customerEntity, CustomerResponse.class)).collect(Collectors.toList());
    }

}
