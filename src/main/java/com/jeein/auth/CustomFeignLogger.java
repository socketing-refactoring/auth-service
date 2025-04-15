package com.jeein.auth;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomFeignLogger extends feign.Logger {

    @Override
    protected void log(String configKey, String format, Object... args) {
        log.info(String.format(methodTag(configKey) + format, args));
    }
}
