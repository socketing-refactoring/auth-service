package com.jeein.auth.exception;

import lombok.Getter;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_REQUEST_VALUE(HttpStatus.BAD_REQUEST, "C_001", "요청 데이터를 모두 올바르게 입력했는지 확인해 주세요."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C_002", "요청 데이터의 타입이 올바르지 않습니다."),
    DATABASE_CONSTRAINT_ERROR(HttpStatus.CONFLICT, "C_003", "요청 데이터의 값이 올바르지 않습니다."),
    INVALID_CONTENT_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "C_004", "지원되지 않는 미디어 타입입니다."),
    PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "C_005", "요청 데이터의 크기가 너무 큽니다."),
    MULTIPART_NO_BOUNDARY(HttpStatus.BAD_REQUEST, "C_006", "요청 헤더의 content type을 확인해 주세요."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S_001", "서버에 오류가 발생했습니다."),
    REQUEST_MAPPING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S_002", "요청 데이터 처리에 오류가 발생했습니다"),
    TOKEN_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S_003", "로그인 과정에서 오류가 발생했습니다."),
    AUTH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S_004", "인증 과정에서 오류가 발생했습니다."),
    
    PASSWORD_FAILED(HttpStatus.FORBIDDEN, "A_001", "비밀번호를 다시 확인해 주세요."),
    INVALID_TOKEN(HttpStatus.FORBIDDEN, "A_002", "접근이 허용되지 않은 사용자입니다"),
    EXPIRED_TOKEN(HttpStatus.FORBIDDEN, "A_003", "만료된 토큰입니다."),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M_001", "회원 정보를 찾을 수 없습니다."),
    EXISTED_PASSWORD(HttpStatus.BAD_REQUEST, "M_002", "새로운 비밀번호를 입력해 주세요.");

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
