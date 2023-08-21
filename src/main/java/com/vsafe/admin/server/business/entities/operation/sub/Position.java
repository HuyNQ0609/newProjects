package com.vsafe.admin.server.business.entities.operation.sub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Position {

    @Field(name = "lat")
    private Double lat;

    @Field(name = "lng")
    private Double lng;
}
