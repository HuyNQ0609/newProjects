package com.vsafe.admin.server.helpers.validators;


import com.vsafe.admin.server.core.exceptions.BadRequestException;
import com.vsafe.admin.server.business.response.BaseResponse;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static java.util.Objects.nonNull;

public interface IBaseValidator<T> {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    void validate(T t);

    default void validate(T t, Class<?> clazz) {};

    default BaseResponse buildResponse(BaseResponse response, Object data) {
        response.setData(data);
        return response;
    }

    default void validateModel(T model, Class<?>... clazz) throws BadRequestException {
        Set<ConstraintViolation<T>> constraintViolations = factory.getValidator().validate(model, clazz);
        executeValidate(constraintViolations);
    }

    default void executeValidate(Set<ConstraintViolation<T>> constraintViolations) throws BadRequestException {
        if (nonNull(constraintViolations) && constraintViolations.size() > 0) {
            ConstraintViolation<T> constraintViolation = constraintViolations.iterator().next();
            throw new BadRequestException(constraintViolation.getMessage());
        }
    }
}
