package com.jeein.auth.exception;

import com.jeein.auth.dto.common.CommonResponseDTO;
import java.util.ArrayList;
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
        CommonResponseDTO<Object> response = CommonResponseDTO.error(ErrorCode.INVALID_REQUEST_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<CommonResponseDTO<Object>> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e) {
        CommonResponseDTO<Object> response = CommonResponseDTO.error(e);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomJwtException.class)
    protected ResponseEntity<CommonResponseDTO<Object>> handleCustomJwtException(CustomJwtException e) {
        CommonResponseDTO<Object> response = CommonResponseDTO.error(e.getErrorCode(), new ArrayList<>());
        return new ResponseEntity<>(response, e.getErrorCode().getStatus());
    }
}
