package com.jeein.auth.feign;

import com.jeein.auth.dto.common.CommonResponseDTO;
import com.jeein.auth.dto.request.JoinRequestDTO;
import com.jeein.auth.dto.request.LoginRequestDTO;
import com.jeein.auth.dto.response.JoinResponseDTO;
import com.jeein.auth.dto.response.MemberLoginResponseDTO;
import com.jeein.auth.dto.response.ValidateTokenResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "member-service")
public interface ManagerServiceFeignClient {

    @PostMapping("/api/v1/managers/join")
    ResponseEntity<CommonResponseDTO<JoinResponseDTO>> joinManager(@RequestBody JoinRequestDTO joinRequestDTO);

    @PostMapping("/api/v1/managers/login")
    ResponseEntity<CommonResponseDTO<MemberLoginResponseDTO>> loginManager(
            @RequestBody LoginRequestDTO loginRequestDTO);

    @PostMapping("/api/v1/managers/validate")
    ResponseEntity<CommonResponseDTO<ValidateTokenResponseDTO>> validateManagerToken(@RequestBody String id);
}
