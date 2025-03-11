package com.jeein.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinResponseDTO {
  private String id;
  private String nickname;
  private String email;
}
