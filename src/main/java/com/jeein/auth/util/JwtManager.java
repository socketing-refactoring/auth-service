package com.jeein.auth.util;

import com.jeein.auth.exception.CustomJwtException;
import com.jeein.auth.exception.ErrorCode;
import io.jsonwebtoken.Jwts;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtManager {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JwtManager(
        String privateKeyPem,
        String publicKeyPem
    ) {
        log.debug("private key pem: {}", privateKeyPem);
        log.debug("public key pem: {}", publicKeyPem);

        try {
            if (privateKeyPem == null || privateKeyPem.isBlank()) {
                throw new IllegalArgumentException("Private key is missing in configuration.");
            }
            if (publicKeyPem == null || publicKeyPem.isBlank()) {
                throw new IllegalArgumentException("Public key is missing in configuration.");
            }

            this.privateKey = getPrivateKeyFromString(privateKeyPem);
            this.publicKey = getPublicKeyFromString(publicKeyPem);
        } catch (Exception e) {
            log.error("Failed to initialize JwtTokenManager: {}", e.getMessage(), e);
            throw new CustomJwtException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public String generateToken(String id, String email) throws CustomJwtException {
        long expireTime = Duration.ofHours(6).toMillis();

        log.debug("jwt 생성 중... id: {}", id);
        log.debug("jwt 생성 중... email: {}", email);

        return Jwts.builder()
            .subject(id)
            .claim("email", email)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expireTime))
            .signWith(privateKey)
            .compact();
    }

    public static PrivateKey getPrivateKeyFromString(String privateKeyPem) throws Exception {
        // PEM 형식에서 Base64 부분만 추출
        String privateKeyPEM = privateKeyPem
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s", ""); // 공백 제거

        // Base64 디코딩
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPEM);

        // PKCS8EncodedKeySpec을 사용하여 PrivateKey 객체 생성
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey getPublicKeyFromString(String publicKeyPem) throws Exception {
        // PEM 형식에서 Base64 부분만 추출
        String publicKeyPEM = publicKeyPem
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s", ""); // 공백 제거

        // Base64 디코딩
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);

        // X509EncodedKeySpec을 사용하여 PublicKey 객체 생성
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public String getStringKeyFromPublicKey() {
        byte[] publicKeyBytes = publicKey.getEncoded();
        String stringPublicKey = Base64.getEncoder().encodeToString(publicKeyBytes);
        log.debug("Base64 기반의 인코딩 결과: {}", stringPublicKey);

        return stringPublicKey;
    }
}
