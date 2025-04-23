package com.jeein.auth.docs.snippets;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.snippet.Attributes.key;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.headers.ResponseHeadersSnippet;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class AuthSnippet {

    public static final RequestFieldsSnippet AUTH_JOIN_REQUEST_FIELDS = requestFields(
                    fieldWithPath("email").type("String").description("이메일")
                                    .attributes(key("constraints").value("형식: example@domain.com")),
                    fieldWithPath("name").type("String").description("이름")
                                    .attributes(key("constraints").value("1자 이상 20자 이하")),
                    fieldWithPath("nickname").type("String").optional().description("닉네임")
                                    .attributes(key("constraints").value("1자 이상 20자 이하")),
                    fieldWithPath("password").type("String").description("비밀번호")
                                    .attributes(key("constraints").value("6자 이상 20자 이하")));

    public static final RequestFieldsSnippet AUTH_LOGIN_REQUEST_FIELDS = requestFields(
                    fieldWithPath("email").description("이메일"), fieldWithPath("password").description("비밀번호"));

    public static final ResponseHeadersSnippet AUTH_LOGIN_RESPONSE_COOKIE =
                    responseHeaders(headerWithName(HttpHeaders.SET_COOKIE)
                                    .description("매니저 로그인 성공 시 발급되는 인증 토큰을 담은 쿠키: managerToken"));

    public static final ResponseFieldsSnippet AUTH_LOGIN_RESPONSE_FIELDS = CommonSnippet
                    .successResponseFields().and(fieldWithPath("data.expiresIn").description("인증 토큰 유효 시간"),
                                    fieldWithPath("data.accessToken").description("인증 토큰"),
                                    fieldWithPath("data.tokenType").description("인증 토큰 타입"));

}
