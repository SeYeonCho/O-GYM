package com.B305.ogym.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import com.B305.ogym.common.annotation.WithAuthUser;
import com.B305.ogym.common.config.SecurityConfig;
import com.B305.ogym.controller.dto.HealthDto.MyHealthResponse;
import com.B305.ogym.controller.dto.HealthDto.MyStudentsHealthListResponse;
import com.B305.ogym.exception.user.UserNotFoundException;
import com.B305.ogym.service.HealthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;


@ExtendWith(RestDocumentationExtension.class) // JUnit 5 사용시 문서 스니펫 생성용
@WebMvcTest(controllers = HealthApiController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}
)
@MockBean(JpaMetamodelMappingContext.class) // @EnableJPaAuditing 사용시 추가해야하는 어노테이션
class HealthApiControllerTest {

    @MockBean
    private HealthService healthService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .apply(sharedHttpSession())
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .build();
    }

    @WithAuthUser(email = "teacher@naver.com", role = "ROLE_PTTEACHER")
    @DisplayName("내 학생들의 건강 정보 확인 - 성공")
    @Test
    public void getMyStudentsHealth_success() throws Exception {
        MyStudentsHealthListResponse healthListResponse = new MyStudentsHealthListResponse();

        given(healthService.findMyStudentsHealth(any())).willReturn(healthListResponse);

        mockMvc.perform(get("/api/health/mystudents"))
            .andDo(print())
            .andExpect(status().isOk());

    }


    @WithAuthUser(email = "student@naver.com", role = "ROLE_PTSTUDENT")
    @DisplayName("내 건강 정보 확인 - 성공")
    @Test
    public void getMyHealth_success() throws Exception {
        MyHealthResponse healthResponse = MyHealthResponse.builder().build();

        given(healthService.getMyHealth(any())).willReturn(healthResponse);

        mockMvc.perform(get("/api/health/myhealth"))
            .andDo(print())
            .andExpect(status().isOk());

    }

    @WithAuthUser(email = "student@naver.com", role = "ROLE_PTSTUDENT")
    @DisplayName("내 건강 정보 확인 - 실패")
    @Test
    public void getMyHealth_failure() throws Exception {
        MyHealthResponse healthResponse = MyHealthResponse.builder().build();

        given(healthService.getMyHealth(any()))
            .willThrow(new UserNotFoundException("회원이 존재하지 않습니다"));

        mockMvc.perform(get("/api/health/myhealth"))
            .andDo(print())
            .andExpect(status().isNotFound());

    }


}