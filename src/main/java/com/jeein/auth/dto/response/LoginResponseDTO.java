package com.jeein.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDTO {
    private long expiresIn;
    private String accessToken;
    private String tokenType;

    //    private String id;
    //    private String nickname;
    //    private String email;
    //    private String token;
}
