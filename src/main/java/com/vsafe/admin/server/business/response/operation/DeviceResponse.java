package com.vsafe.admin.server.business.response.operation;

import com.vsafe.admin.server.business.entities.operation.DeviceConfigEntity;
import com.vsafe.admin.server.business.entities.operation.DeviceStatusEntity;
import com.vsafe.admin.server.business.entities.operation.sub.SensorEntity;
import com.vsafe.admin.server.core.annotations.ExcelColumn;
import com.vsafe.admin.server.helpers.utils.mapper.MapperFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceResponse {

    @ExcelColumn()
    private String id;

    @ExcelColumn()
    private String code;

    @ExcelColumn()
    private String title;

    @ExcelColumn()
    private Integer status;

    @ExcelColumn()
    private String description;

    @ExcelColumn(isHidden = true)
    private List<SensorEntity> sensorList;

    @ExcelColumn(isHidden = true)
    private String clientId;

    @ExcelColumn(isHidden = true)
    private String responseTime;

    public static DeviceResponse of(DeviceConfigEntity deviceEntity) {
        ModelMapper mapper = MapperFactory.mapper(MapperFactory.ModelMapperOptions.DEEP_COPY);
        return mapper.map(deviceEntity, DeviceResponse.class);
    }

    public static List<DeviceResponse> of(List<DeviceConfigEntity> deviceEntities) {
        ModelMapper mapper = MapperFactory.mapper(MapperFactory.ModelMapperOptions.DEEP_COPY);
        return deviceEntities.stream().map(deviceEntity -> mapper.map(deviceEntity, DeviceResponse.class)).collect(Collectors.toList());
    }

    public static List<DeviceResponse> of(List<DeviceConfigEntity> deviceEntities, List<DeviceStatusEntity> deviceStatusEntities) {
        ModelMapper mapper = MapperFactory.mapper(MapperFactory.ModelMapperOptions.DEEP_COPY);
        List<DeviceResponse> deviceResponses = deviceEntities.stream().map(deviceEntity -> mapper.map(deviceEntity, DeviceResponse.class)).collect(Collectors.toList());
        deviceResponses.forEach(item -> {
            DeviceStatusEntity deviceStatusEntity = deviceStatusEntities.stream().filter(device -> device.getDeviceId().equals(item.getId())).findFirst().orElse(null);
            if (Objects.nonNull(deviceStatusEntity)) {
                long diff = (new Date()).getTime() - deviceStatusEntity.getResponseTime().getTime();
                long diffDays = TimeUnit.MILLISECONDS.toDays(diff);
                long diffHours = TimeUnit.MILLISECONDS.toHours(diff);
                long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                String timeDistance = diffDays > 0 ? diffDays + " ngày trước" : diffHours > 0 ? diffHours + " giờ trước" : diffMinutes + " phút trước";
                item.setResponseTime(timeDistance);
            }
        });
        return deviceResponses;
    }

}
