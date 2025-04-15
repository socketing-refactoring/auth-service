package com.jeein.auth.controller;

import com.jeein.auth.dto.common.CommonResponseDTO;
import com.jeein.auth.dto.request.JoinRequestDTO;
import com.jeein.auth.dto.request.LoginRequestDTO;
import com.jeein.auth.dto.response.JoinResponseDTO;
import com.jeein.auth.dto.response.LoginResponseDTO;
import com.jeein.auth.dto.response.ValidateTokenResponseDTO;
import com.jeein.auth.service.AuthService;
import com.jeein.auth.service.ManagerAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/manager")
@RequiredArgsConstructor
public class ManagerAuthController {

    private final ManagerAuthService managerAuthService;

    // 관리자 가입
    @PostMapping("/join")
    public ResponseEntity<CommonResponseDTO<JoinResponseDTO>> managerJoin(
            @RequestBody @Valid JoinRequestDTO managerJoinRequestDTO) {
        return ResponseEntity.ok(managerAuthService.registerManager(managerJoinRequestDTO));
    }

    // 관리자 로그인
    @PostMapping("/login")
    public ResponseEntity<CommonResponseDTO<LoginResponseDTO>> managerLogin(
            @RequestBody @Valid LoginRequestDTO managerLoginRequestDTO) {
        return ResponseEntity.ok(managerAuthService.loginManager(managerLoginRequestDTO));
    }

    // 관리자 토큰 검증
    @GetMapping("/validate")
    public ResponseEntity<CommonResponseDTO<ValidateTokenResponseDTO>> validateManagerToken(
            @RequestParam String token) {
        return ResponseEntity.ok(managerAuthService.validateManagerToken(token));
    }
}
