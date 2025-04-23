package com.jeein.auth.controller;

import com.jeein.auth.dto.common.CommonResponseDTO;
import com.jeein.auth.dto.request.JoinRequestDTO;
import com.jeein.auth.dto.request.LoginRequestDTO;
import com.jeein.auth.dto.response.JoinResponseDTO;
import com.jeein.auth.dto.response.LoginResponseDTO;
import com.jeein.auth.dto.response.ValidateTokenResponseDTO;
import com.jeein.auth.service.ManagerAuthService;
import jakarta.validation.Valid;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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

        CommonResponseDTO<LoginResponseDTO> response =
                        managerAuthService.loginManager(managerLoginRequestDTO);

        String token = response.getData().getAccessToken();

        ResponseCookie cookie = ResponseCookie.from("managerToken", token).httpOnly(true).path("/")
                        .domain(".jeein.xyz").maxAge(Duration.ofHours(6)).sameSite("None").secure(true)
                        .build();

        log.debug(cookie.getName(), cookie.getValue());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponseDTO<Void>> managerLogout() {

        CommonResponseDTO<Void> response = CommonResponseDTO.success("Manager Logout successful", "0", null);

        ResponseCookie expiredCookie = ResponseCookie.from("managerToken", "").httpOnly(true).path("/")
                        .domain(".jeein.xyz").maxAge(0).sameSite("None").secure(true).build();

        log.debug(expiredCookie.getName(), expiredCookie.getValue());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, expiredCookie.toString()).body(response);
    }

    // 관리자 토큰 검증
    @GetMapping("/validate")
    public ResponseEntity<CommonResponseDTO<ValidateTokenResponseDTO>> validateManagerToken(
                    @RequestParam String token) {
        return ResponseEntity.ok(managerAuthService.validateManagerToken(token));
    }
}
