package com.jeein.auth.controller;

import com.jeein.auth.dto.common.CommonResponseDTO;
import com.jeein.auth.dto.request.JoinRequestDTO;
import com.jeein.auth.dto.response.JoinResponseDTO;
import com.jeein.auth.dto.response.LoginResponseDTO;
import com.jeein.auth.dto.response.ValidateTokenResponseDTO;
import com.jeein.auth.dto.request.LoginRequestDTO;
import com.jeein.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<CommonResponseDTO<JoinResponseDTO>> join(@RequestBody @Valid JoinRequestDTO joinRequestDTO) {
        return ResponseEntity.ok(authService.registerMember(joinRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDTO<LoginResponseDTO>> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.loginMember(loginRequestDTO));
    }

    @GetMapping("/validate")
    public ResponseEntity<CommonResponseDTO<ValidateTokenResponseDTO>> validateToken(@RequestParam String token) {
      return ResponseEntity.ok(authService.validateToken(token));
    }
}
