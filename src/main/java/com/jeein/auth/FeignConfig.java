package com.jeein.auth;

import feign.Logger;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class FeignConfig {

    // @Bean
    // public ErrorDecoder errorDecoder() {
    // return new CustomErrorDecoder();
    // }

    @Bean
    Logger feignLogger() {
        return new CustomFeignLogger();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
