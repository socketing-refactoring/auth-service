package com.jeein.auth.feign;

import com.jeein.auth.dto.common.CommonResponseDTO;
import com.jeein.auth.dto.request.JoinRequestDTO;
import com.jeein.auth.dto.request.LoginRequestDTO;
import com.jeein.auth.dto.response.JoinResponseDTO;
import com.jeein.auth.dto.response.MemberLoginResponseDTO;
import com.jeein.auth.dto.response.ValidateTokenResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "member-service")
public interface MemberServiceFeignClient {

    @PostMapping("/api/v1/members/join")
    CommonResponseDTO<JoinResponseDTO> createMember(@RequestBody JoinRequestDTO joinRequestDTO);

    @PostMapping("/api/v1/members/login")
    CommonResponseDTO<MemberLoginResponseDTO> loginMember(
            @RequestBody LoginRequestDTO loginRequestDTO);

    @PostMapping("/api/v1/members/validate")
    CommonResponseDTO<ValidateTokenResponseDTO> validateMemberById(@RequestBody String id);
}
