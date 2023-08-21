package com.vsafe.admin.server.business.response.operation;

import com.vsafe.admin.server.business.entities.operation.AreaEntity;
import com.vsafe.admin.server.business.entities.operation.sub.Position;
import com.vsafe.admin.server.business.entities.operation.sub.Representative;
import com.vsafe.admin.server.core.annotations.ExcelColumn;
import com.vsafe.admin.server.helpers.utils.mapper.MapperFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarkerResponse {

    @ExcelColumn()
    private String id;

    @ExcelColumn()
    private Position position;

    @ExcelColumn()
    private String address;

    @ExcelColumn()
    private String title;

    @ExcelColumn()
    private String tag;
    @ExcelColumn()
    private Integer status;

    @ExcelColumn()
    private String description;

    @ExcelColumn()
    private String markerImage;

    @ExcelColumn()
    private String phone;

    @Email
    @ExcelColumn()
    private String email;

    @ExcelColumn(isHidden = true)
    private Representative representative;

    private String fireFighterId;

    private String provinceCode;

    public static MarkerResponse of(AreaEntity markerEntity) {
        if (Objects.isNull(markerEntity)) return null;
        ModelMapper mapper = MapperFactory.mapper(MapperFactory.ModelMapperOptions.DEEP_COPY);
        return mapper.map(markerEntity, MarkerResponse.class);
    }

    public static List<MarkerResponse> of(List<AreaEntity> markerEntities) {
        if (CollectionUtils.isEmpty(markerEntities)) return new ArrayList<>();
        ModelMapper mapper = MapperFactory.mapper(MapperFactory.ModelMapperOptions.DEEP_COPY);
        return markerEntities.stream().map(markerEntity -> mapper.map(markerEntity, MarkerResponse.class)).collect(Collectors.toList());
    }

}
