package com.vsafe.admin.server.helpers.utils.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.ArrayUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;

public class MapperFactory {
    public enum MapperOptions {SORT_FIELD}

    public static ObjectMapper mapper(MapperOptions... options) {
        ObjectMapper mapper = new ObjectMapper();
        if (options != null &&
                ArrayUtils.contains(options, MapperOptions.SORT_FIELD)) {
            mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        }

        return mapper;
    }

    public enum ModelMapperOptions {DEEP_COPY, SKIP_NULL}

    public static ModelMapper mapper(ModelMapperOptions... option) {
        return mapper(MatchingStrategies.STRICT, option);
    }

    public static ModelMapper mapper(MatchingStrategy strategies, ModelMapperOptions... option) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(strategies);
        if (option != null && ArrayUtils.contains(option, ModelMapperOptions.DEEP_COPY))
            mapper.getConfiguration().setDeepCopyEnabled(true);
        if (option != null && ArrayUtils.contains(option, ModelMapperOptions.SKIP_NULL))
            mapper.getConfiguration().setDeepCopyEnabled(true);
        return mapper;
    }
}
