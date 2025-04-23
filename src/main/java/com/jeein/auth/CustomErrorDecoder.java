// package com.jeein.auth;
//
// import feign.Response;
// import feign.codec.ErrorDecoder;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Component;
//
// @Slf4j
// @Component
// public class CustomErrorDecoder implements ErrorDecoder {
//
// @Override
// public Exception decode(String methodKey, Response response) {
// if (response.status() >= 400) {
// log.info("에러 디코딩 중");
// return null;
// }
//
// return new ErrorDecoder.Default().decode(methodKey, response);
// }
// }
