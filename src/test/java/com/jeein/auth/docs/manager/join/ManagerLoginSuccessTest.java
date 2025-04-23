package com.jeein.auth.docs.manager.join;

import static com.jeein.auth.docs.util.RestDocsUtil.doc;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeein.auth.ResponseMessage;
import com.jeein.auth.docs.constants.ManagerApiPath;
import com.jeein.auth.docs.constants.ManagerDocumentIdentifier;
import com.jeein.auth.docs.snippets.AuthSnippet;
import com.jeein.auth.dto.common.CommonResponseDTO;
import com.jeein.auth.dto.request.JoinRequestDTO;
import com.jeein.auth.dto.request.LoginRequestDTO;
import com.jeein.auth.dto.response.JoinResponseDTO;
import com.jeein.auth.feign.MemberServiceFeignClient;
import com.jeein.auth.service.ManagerAuthService;
import feign.FeignException.FeignClientException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
// @ActiveProfiles("test")
@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
@DisplayName("로그인 성공 테스트")
public class ManagerLoginSuccessTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ManagerAuthService managerAuthService;

    @Autowired
    private MemberServiceFeignClient memberServiceFeignClient;

    @Autowired
    private ObjectMapper objectMapper;

    private String testManagerId;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
                    RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                        .apply(documentationConfiguration(restDocumentation))
                        .defaultRequest(get("/").accept(MediaType.APPLICATION_JSON)
                                        .contentType(MediaType.APPLICATION_JSON))
                        .build();

        // 기존 회원 생성
        JoinRequestDTO request = JoinRequestDTO.of("test@example.com", "이름", "닉네임", "password");
        CommonResponseDTO<JoinResponseDTO> response = managerAuthService.registerManager(request);
        this.testManagerId = response.getData().getId();
        log.debug(testManagerId);
    }

    @AfterEach
    void cleanUp() {
        if (testManagerId == null) {
            return;
        }

        try {
            memberServiceFeignClient.hardDeleteManager(testManagerId);
        } catch (FeignClientException e) {
            log.debug(e.getMessage());
        }
    }

    @Test
    @DisplayName("로그인 요청이 유효하고 회원가입 정보와 일치하면 로그인에 성공한다.")
    void login_withInvalidEmail_shouldReturnForbidden() throws Exception {
        LoginRequestDTO loginRequest = LoginRequestDTO.of("test@example.com", "password");
        log.debug(loginRequest.toString());

        mockMvc.perform(post(ManagerApiPath.MANAGER_AUTH_LOGIN)
                        .content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isOk())
                        .andExpect(jsonPath("$.code").value("0"))
                        .andExpect(jsonPath("$.message").value(ResponseMessage.LOGIN_SUCCESS))
                        .andExpect(jsonPath("$.errors").doesNotExist())
                        .andExpect(jsonPath("$.data.expiresIn").exists())
                        .andExpect(jsonPath("$.data.accessToken").exists())
                        .andExpect(jsonPath("$.data.tokenType").exists())
                        .andExpect(cookie().exists("managerToken"))
                        // .andExpect(header().string(HttpHeaders.SET_COOKIE, notNullValue()))
                        .andDo(doc(ManagerDocumentIdentifier.LOGIN_SUCCESS_BASE,
                                        AuthSnippet.AUTH_LOGIN_REQUEST_FIELDS,
                                        AuthSnippet.AUTH_LOGIN_RESPONSE_COOKIE,
                                        AuthSnippet.AUTH_LOGIN_RESPONSE_FIELDS));
    }
}
