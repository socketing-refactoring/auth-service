package com.jeein.auth.exception;

import com.jeein.auth.dto.common.CommonResponseDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MemberFeignException extends RuntimeException {
    private final HttpStatus status;
    private final CommonResponseDTO responseBody;

    public MemberFeignException(HttpStatus status, CommonResponseDTO responseBody) {
        super("Feign Client Error: " + responseBody);

        this.status = status;
        this.responseBody = responseBody;
    }
}
