package com.jeein.auth.service;

import com.jeein.auth.dto.common.CommonResponseDTO;
import com.jeein.auth.dto.request.JoinRequestDTO;
import com.jeein.auth.dto.request.LoginRequestDTO;
import com.jeein.auth.dto.response.JoinResponseDTO;
import com.jeein.auth.dto.response.LoginResponseDTO;
import com.jeein.auth.dto.response.MemberLoginResponseDTO;
import com.jeein.auth.dto.response.ValidateTokenResponseDTO;
import com.jeein.auth.exception.CustomJwtException;
import com.jeein.auth.exception.ErrorCode;
import com.jeein.auth.feign.MemberServiceFeignClient;
import com.jeein.auth.util.JwtManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import java.security.PublicKey;
import java.util.Date;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtManager jwtManager;
    private final MemberServiceFeignClient memberServiceFeignClient;

    // 회원 가입
    public CommonResponseDTO<JoinResponseDTO> registerMember(@RequestBody @Valid JoinRequestDTO joinRequestDTO) {
        CommonResponseDTO<JoinResponseDTO> response = memberServiceFeignClient.createMember(joinRequestDTO);

        if (!response.getCode().equals("0")) {
            return response;
        } else if (response == null || response.getData() == null) {
            throw new CustomJwtException(ErrorCode.AUTH_FAILED);
        }

        return response;
    }

    // 로그인
    public CommonResponseDTO<LoginResponseDTO> loginMember(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {

        CommonResponseDTO<MemberLoginResponseDTO> memberLoginResponse = memberServiceFeignClient.loginMember(loginRequestDTO);
        if (!memberLoginResponse.getCode().equals("0")) {

            return CommonResponseDTO.<LoginResponseDTO>builder()
                .code(memberLoginResponse.getCode())
                .message(memberLoginResponse.getMessage())
                .data(null) // 실패 응답일 경우 data는 null
                .build();
        } else if (memberLoginResponse == null || memberLoginResponse.getData() == null) {
            throw new CustomJwtException(ErrorCode.AUTH_FAILED);
        }

        String token = jwtManager.generateToken(memberLoginResponse.getData().getId(), memberLoginResponse.getData().getEmail());
        ResponseCookie jwtCookie = ResponseCookie.from("auth-token", token)
            .httpOnly(true)
            .secure(true)
            .path("/")        // 모든 경로에서 사용 가능
            .maxAge(6 * 60 * 60)
            .sameSite("Strict")
            .build();

        LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
            .id(memberLoginResponse.getData().getId())
            .nickname(memberLoginResponse.getData().getNickname())
            .email(memberLoginResponse.getData().getEmail())
            .token(token)
            .build();
        return CommonResponseDTO.success("로그인이 성공적으로 이루어졌습니다.", "0", loginResponseDTO);
    }

    // 토큰 유효성 검사
    public CommonResponseDTO<ValidateTokenResponseDTO> validateToken(String token) {
        PublicKey publicKey = jwtManager.getPublicKey();

        Claims claims = null;
        try {
            claims = Jwts.parser()
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

        CommonResponseDTO<ValidateTokenResponseDTO> response = memberServiceFeignClient.validateMemberById(claims.getSubject());

        if (!response.getCode().equals("0")) {
            return response;
        } else if (response == null || response.getData() == null) {
            throw new CustomJwtException(ErrorCode.AUTH_FAILED);
        }

        return response;
    }
}
