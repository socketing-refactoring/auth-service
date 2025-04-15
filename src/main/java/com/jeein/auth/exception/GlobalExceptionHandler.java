package com.jeein.auth.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeein.auth.dto.common.CommonResponseDTO;
import java.util.ArrayList;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<CommonResponseDTO<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        CommonResponseDTO<Object> response =
                CommonResponseDTO.error(ErrorCode.INVALID_REQUEST_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<CommonResponseDTO<Object>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        CommonResponseDTO<Object> response = CommonResponseDTO.error(e);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomJwtException.class)
    protected ResponseEntity<CommonResponseDTO<Object>> handleCustomJwtException(
            CustomJwtException e) {
        CommonResponseDTO<Object> response =
                CommonResponseDTO.error(e.getErrorCode());
        return new ResponseEntity<>(response, e.getErrorCode().getStatus());
    }

    @ExceptionHandler(FeignException.class)
    protected ResponseEntity<CommonResponseDTO<Object>> handleFeignException(
            FeignException e) {
        CommonResponseDTO<Object> response =
                CommonResponseDTO.error(ErrorCode.FEIGN_CLIENT_ERROR);
        log.info(e.getMessage());
        log.info("feign exception");
        return new ResponseEntity<>(response, ErrorCode.FEIGN_CLIENT_ERROR.getStatus());
    }

    @ExceptionHandler(MemberFeignException.class)
    protected ResponseEntity<CommonResponseDTO<Object>> handleMemberFeignException(
            MemberFeignException e) {
        log.info(e.getMessage());
        log.info("member feign exception");
        return new ResponseEntity<>(e.getResponseBody(), e.getStatus());
    }

    @ExceptionHandler(AuthException.class)
    protected ResponseEntity<CommonResponseDTO<Object>> handleAuthException(
            AuthException e) {
        CommonResponseDTO<Object> response =
                CommonResponseDTO.error(ErrorCode.FEIGN_CLIENT_ERROR);
        return new ResponseEntity<>(response, ErrorCode.FEIGN_CLIENT_ERROR.getStatus());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CommonResponseDTO<Object>> handleException(
            Exception e) {
        CommonResponseDTO<Object> response =
                CommonResponseDTO.error(ErrorCode.INTERNAL_SERVER_ERROR);
        log.info(e.getMessage());
        log.info("exception.class");
        return new ResponseEntity<>(response, ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }
}
