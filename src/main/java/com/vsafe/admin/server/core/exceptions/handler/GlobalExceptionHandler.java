package com.vsafe.admin.server.core.exceptions.handler;

import com.vsafe.admin.server.core.exceptions.BadRequestException;
import com.vsafe.admin.server.core.exceptions.NotFoundException;
import com.vsafe.admin.server.core.exceptions.SystemException;
import com.vsafe.admin.server.helpers.enums.responseStatus.ResponseStatus;
import com.vsafe.admin.server.business.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.*;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // 405
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers,
            final HttpStatus status, final WebRequest request) {
        ex.printStackTrace();
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        Objects.requireNonNull(ex.getSupportedHttpMethods()).forEach(t -> builder.append(t).append(" "));

        BaseResponse response = BaseResponse.builder()
                .messageCode(HttpStatus.METHOD_NOT_ALLOWED.name())
                .message(List.of(builder.toString()))
                .build();
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    //404
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ex.printStackTrace();
        BaseResponse response = BaseResponse.builder()
                .message(List.of(HttpStatus.NOT_FOUND.name()))
                .message(List.of(ex.getRequestURL() + ": not found"))
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Validate exception

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ex.printStackTrace();
        BaseResponse response = BaseResponse.error(ResponseStatus.INVALID, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage());
        BaseResponse response = BaseResponse.error(ResponseStatus.INVALID, "Request không hợp lệ");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BaseResponse response = BaseResponse.error(ResponseStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<BaseResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }

        return new ResponseEntity<>(BaseResponse.error(ResponseStatus.INVALID, errors), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ex.printStackTrace();
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            String errorMessage =error.getField() +" "+ error.getDefaultMessage();
            errors.add(errorMessage);
        });
        return new ResponseEntity<>(BaseResponse.error(ResponseStatus.INVALID, errors, null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SystemException.class)
    protected ResponseEntity<BaseResponse> handleSystemException(SystemException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(BaseResponse.error(ResponseStatus.SYSTEM_ERROR, ex.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse> handle(Exception ex,
                                               HttpServletRequest request, HttpServletResponse response) {
        ex.printStackTrace();
        return new ResponseEntity<>(BaseResponse.error(ResponseStatus.SYSTEM_ERROR, "Lỗi hệ thống"), HttpStatus.OK);
    }
}
