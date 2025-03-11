package com.jeein.auth.dto.common;

import com.jeein.auth.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class CommonResponseDTO<T> {
  private String message;
  private String code;
  private List<FieldError> errors;
  private T data;

  // 성공 시 사용할 생성자
  private CommonResponseDTO(String message, String code, T data) {
    this.message = message;
    this.code = code;
    this.errors = new ArrayList<>();
    this.data = data;
  }

  // 실패 시 사용할 생성자
  private CommonResponseDTO(String message, String code, List<FieldError> errors) {
    this.message = message;
    this.code = code;
    this.errors = errors;
    this.data = null;
  }

  // 성공 응답을 위한 팩토리 메소드
  public static <T> CommonResponseDTO<T> success(String message, String code, T data) {
    return new CommonResponseDTO<>(message, code, data);
  }

  // 실패 응답을 위한 팩토리 메소드
  public static CommonResponseDTO<Object> error(ErrorCode errorCode, List<FieldError> errors) {
    return new CommonResponseDTO<>(errorCode.getMessage(), errorCode.getCode(), errors);
  }

  // 실패 응답을 위한 팩토리 메소드 (ErrorCode와 BindingResult를 이용한 처리)
  public static CommonResponseDTO<Object> error(ErrorCode errorCode, BindingResult bindingResult) {
    List<FieldError> fieldErrors = FieldError.of(bindingResult);
    return new CommonResponseDTO<>(errorCode.getMessage(), errorCode.getCode(), fieldErrors);
  }

  // MethodArgumentTypeMismatchException을 처리하는 메소드
  public static CommonResponseDTO<Object> error(MethodArgumentTypeMismatchException e) {
    String value = Optional.ofNullable(e.getValue())
        .map(Object::toString)
        .orElse("");
    List<FieldError> errors = FieldError.of(e.getName(), value, e.getErrorCode());
    return new CommonResponseDTO<>(ErrorCode.INVALID_TYPE_VALUE.getMessage(),
        ErrorCode.INVALID_TYPE_VALUE.getCode(), errors);
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class FieldError {
    private String field;
    private String value;
    private String reason;

    private FieldError(String field, String value, String reason) {
      this.field = field;
      this.value = value;
      this.reason = reason;
    }

    public static List<FieldError> of(String field, String value, String reason) {
      List<FieldError> fieldErrors = new ArrayList<>();
      fieldErrors.add(new FieldError(field, value, reason));
      return fieldErrors;
    }

    private static List<FieldError> of(BindingResult bindingResult) {
      List<org.springframework.validation.FieldError> fieldErrors = bindingResult
          .getFieldErrors();
      return fieldErrors.stream()
          .map(error -> new FieldError(
              error.getField(),
              error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
              error.getDefaultMessage()))
          .collect(Collectors.toList());
    }
  }
}
