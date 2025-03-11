package com.jeein.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberLoginResponseDTO {
  private String id;
  private String nickname;
  private String email;
}
