package com.jeein.auth;

import com.jeein.auth.util.JwtManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//root-context.xml
@Slf4j
@Configuration
public class AppConfig {

    @Value("${token.jwt.private}")
    private String privateKeyPem;

    @Value("${token.jwt.public}")
    private String publicKeyPem;

    @Bean
    public JwtManager jwtTokenManager() throws Exception {
        log.debug("private key pem: {}", privateKeyPem);
        log.debug("public key pem: {}", publicKeyPem);

        return new JwtManager(privateKeyPem, publicKeyPem);
    }
}
