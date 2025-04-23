package com.jeein.auth.feign;

import com.jeein.auth.dto.common.CommonResponseDTO;
import com.jeein.auth.dto.request.JoinRequestDTO;
import com.jeein.auth.dto.request.LoginRequestDTO;
import com.jeein.auth.dto.response.JoinResponseDTO;
import com.jeein.auth.dto.response.MemberLoginResponseDTO;
import com.jeein.auth.dto.response.ValidateTokenResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "member-service")
public interface MemberServiceFeignClient {

    @PostMapping("/api/v1/members/join")
    ResponseEntity<CommonResponseDTO<JoinResponseDTO>> createMember(
                    @RequestBody JoinRequestDTO joinRequestDTO);

    @PostMapping("/api/v1/members/login")
    ResponseEntity<CommonResponseDTO<MemberLoginResponseDTO>> loginMember(
                    @RequestBody LoginRequestDTO loginRequestDTO);

    @PostMapping("/api/v1/members/validate")
    ResponseEntity<CommonResponseDTO<ValidateTokenResponseDTO>> validateMemberById(@RequestBody String id);


    @PostMapping("/api/v1/managers/join")
    ResponseEntity<CommonResponseDTO<JoinResponseDTO>> joinManager(
                    @RequestBody JoinRequestDTO joinRequestDTO);

    @PostMapping("/api/v1/managers/login")
    ResponseEntity<CommonResponseDTO<MemberLoginResponseDTO>> loginManager(
                    @RequestBody LoginRequestDTO loginRequestDTO);

    @DeleteMapping("/api/v1/managers/{managerId}/hard")
    ResponseEntity<CommonResponseDTO<Void>> hardDeleteManager(@PathVariable String managerId);

    @PostMapping("/api/v1/managers/validate")
    ResponseEntity<CommonResponseDTO<ValidateTokenResponseDTO>> validateManagerToken(@RequestBody String id);

}
