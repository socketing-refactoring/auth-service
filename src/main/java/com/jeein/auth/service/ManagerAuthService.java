package com.jeein.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeein.auth.dto.common.CommonResponseDTO;
import com.jeein.auth.dto.request.JoinRequestDTO;
import com.jeein.auth.dto.request.LoginRequestDTO;
import com.jeein.auth.dto.response.JoinResponseDTO;
import com.jeein.auth.dto.response.LoginResponseDTO;
import com.jeein.auth.dto.response.MemberLoginResponseDTO;
import com.jeein.auth.dto.response.ValidateTokenResponseDTO;
import com.jeein.auth.exception.CustomJwtException;
import com.jeein.auth.exception.ErrorCode;
import com.jeein.auth.exception.GeneralFeignException;
import com.jeein.auth.exception.MemberFeignException;
import com.jeein.auth.feign.ManagerServiceFeignClient;
import com.jeein.auth.feign.MemberServiceFeignClient;
import com.jeein.auth.util.JwtManager;
import feign.FeignException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.PublicKey;
import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerAuthService {

    private final JwtManager jwtManager;
    private final ManagerServiceFeignClient managerServiceFeignClient;
    private final ObjectMapper objectMapper;

    // 회원 가입
    public CommonResponseDTO<JoinResponseDTO> registerManager(
            @RequestBody @Valid JoinRequestDTO joinRequestDTO) {

        ResponseEntity<CommonResponseDTO<JoinResponseDTO>> response = managerServiceFeignClient.joinManager(joinRequestDTO);
        if (response.getStatusCode().isError()) {
            throw new MemberFeignException(HttpStatus.valueOf(response.getStatusCode().value()), response.getBody());
        }

        return response.getBody();
    }

    // 로그인
    public CommonResponseDTO<LoginResponseDTO> loginManager(
            @RequestBody @Valid LoginRequestDTO loginRequestDTO) {

        ResponseEntity<CommonResponseDTO<MemberLoginResponseDTO>> memberLoginResponse;
        try {
             memberLoginResponse = managerServiceFeignClient.loginManager(loginRequestDTO);
        } catch (FeignException e) {
            int status = e.status();
            String responseBody = e.contentUTF8();
            log.warn("Feign 오류 발생, 상태코드: {}, body: {}", e.status(), responseBody);

            if (status == -1 || responseBody == null || responseBody.isBlank()) {
                log.warn("Feign 오류: 응답 없음 또는 상태값이 -1 (네트워크 오류 가능)");
                throw e;
            }

            try {
                // 예: body 파싱해서 custom exception 생성
                CommonResponseDTO<MemberLoginResponseDTO> parsedBody = objectMapper.readValue(
                        responseBody,
                        new TypeReference<CommonResponseDTO<MemberLoginResponseDTO>>() {
                        }
                );

                throw new MemberFeignException(HttpStatus.valueOf(e.status()), parsedBody);
            } catch (JsonProcessingException je) {
                throw new GeneralFeignException(ErrorCode.FEIGN_CLIENT_ERROR);
            }
        }

        if (memberLoginResponse.getBody() == null) {
            throw new GeneralFeignException(ErrorCode.FEIGN_CLIENT_ERROR);
        }

        long expireTime = Duration.ofHours(6).toMillis();
        long expiresIn = System.currentTimeMillis() + expireTime;
        String token =
                jwtManager.generateToken(
                        memberLoginResponse.getBody().getData().getId(),
                        memberLoginResponse.getBody().getData().getEmail(),
                        new Date(expiresIn));

        LoginResponseDTO loginResponseDTO =
                LoginResponseDTO.builder()
                        .accessToken(token)
                        .expiresIn(expiresIn)
                        .tokenType("Bearer")
                        .build();
        return CommonResponseDTO.success("로그인이 성공적으로 이루어졌습니다.", "0", loginResponseDTO);
    }

    // 토큰 유효성 검사
    public CommonResponseDTO<ValidateTokenResponseDTO> validateManagerToken(String token) {
        PublicKey publicKey = jwtManager.getPublicKey();

        Claims claims = null;
        try {
            claims =
                    Jwts.parser()
                            .verifyWith(publicKey)
                            .build()
                            .parseSignedClaims(token)
                            .getPayload();

        } catch (Exception e) {
            log.debug("JWT 인증실패");
            throw new CustomJwtException(ErrorCode.INVALID_TOKEN);
        }

        // 만료 시간 검사
        Date expiration = claims.getExpiration();
        if (expiration.before(new Date())) {
            log.debug("JWT 토큰이 만료되었습니다.");
            throw new CustomJwtException(ErrorCode.EXPIRED_TOKEN);
        }

        ResponseEntity<CommonResponseDTO<ValidateTokenResponseDTO>> response = managerServiceFeignClient.validateManagerToken(claims.getSubject());
        if (response.getStatusCode().isError()) {
            throw new MemberFeignException(HttpStatus.valueOf(response.getStatusCode().value()), response.getBody());
        }

        return response.getBody();
    }
}
