package com.jeein.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidateTokenResponseDTO {
    private String id;
    private String nickname;
}
