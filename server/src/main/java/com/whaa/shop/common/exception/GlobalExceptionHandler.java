package com.whaa.shop.common.exception;

import com.whaa.shop.common.api.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiResponse<Void> business(BusinessException e, HttpServletRequest request) {
        log.warn("Business request failed: method={}, uri={}, message={}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return ApiResponse.fail(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiResponse<Void> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getAllErrors().isEmpty() ? "请求参数无效" : e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn("Request validation failed: method={}, uri={}, message={}", request.getMethod(), request.getRequestURI(), message);
        return ApiResponse.fail(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ApiResponse<Void> unexpected(Exception e, HttpServletRequest request) {
        log.error("Unhandled request error: method={}, uri={}", request.getMethod(), request.getRequestURI(), e);
        return ApiResponse.fail("系统异常，请稍后重试");
    }
}
