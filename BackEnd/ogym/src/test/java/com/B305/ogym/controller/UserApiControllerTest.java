package com.B305.ogym.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import com.B305.ogym.common.config.SecurityConfig;
import com.B305.ogym.controller.dto.UserDto.SaveTeacherRequest;
import com.B305.ogym.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(RestDocumentationExtension.class) // JUnit 5 사용시 문서 스니펫 생성용
@WebMvcTest(controllers = UserApiController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}
)
//@ActiveProfiles("test") // 테스트에서 사용할 profile
@MockBean(JpaMetamodelMappingContext.class) // @EnableJPaAuditing 사용시 추가해야하는 어노테이션
//@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
class UserApiControllerTest {

    @MockBean
    private UserService userService;

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

    @DisplayName("회원가입 - 모든 유효성 검사에 통과했다면, 회원가입 성공")
    @Test
    public void createUser_success() throws Exception {
        //given
        SaveTeacherRequest teacherRequest = SaveTeacherRequest.builder()
            .email("hello@naver.com")
            .password("asdasd")
            .username("juhu")
            .nickname("juhu")
            .gender(0)
            .tel("010-0000-0000")
            .zipCode("12345")
            .street("road 17")
            .detailedAddress("juhu")
            .role("ROLE_PTTEACHER")
            .major("재활")
            .certificates(new ArrayList<>())
            .careers(new ArrayList<>())
            .price(1000)
            .description("설명설명")
            .snsAddrs(new ArrayList<>())
            .build();
        //when
        doNothing().when(userService).signup(teacherRequest);

        //then
        mockMvc.perform(post("/api/user/teacher")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(teacherRequest)))
            .andDo(print())
            .andExpect(status().isOk()) // 201 isCreated()
            .andDo(document("userApi/signup/successful", requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("The user's email address"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("The user's password"),
                fieldWithPath("username").type(JsonFieldType.STRING)
                    .description("The user's username"),
                fieldWithPath("nickname").type(JsonFieldType.STRING)
                    .description("The user's nickname"),
                fieldWithPath("gender").type(JsonFieldType.NUMBER)
                    .description("The user's gender"),
                fieldWithPath("tel").type(JsonFieldType.STRING)
                    .description("The user's tel"),
                fieldWithPath("zipCode").type(JsonFieldType.STRING)
                    .description("The user's zipCode"),
                fieldWithPath("street").type(JsonFieldType.STRING)
                    .description("The user's street"),
                fieldWithPath("detailedAddress").type(JsonFieldType.STRING)
                    .description("The user's detailedAddress"),
                fieldWithPath("role").type(JsonFieldType.STRING)
                    .description("The user's role"),
                fieldWithPath("major").type(JsonFieldType.STRING)
                    .description("The user's major"),
                fieldWithPath("certificates").type(JsonFieldType.ARRAY)
                    .description("The user's certificates"),
                fieldWithPath("careers").type(JsonFieldType.ARRAY)
                    .description("The user's careers"),
                fieldWithPath("price").type(JsonFieldType.NUMBER)
                    .description("The user's price"),
                fieldWithPath("description").type(JsonFieldType.STRING)
                    .description("The user's description"),
                fieldWithPath("snsAddrs").type(JsonFieldType.ARRAY)
                    .description("The user's snsAddrs")
            )));

    }

    @DisplayName("회원가입 - 이메일 중복으로 인한 회원가입 실패")
    @Test
    public void createUser_failure() throws Exception {
        //given

        //when

        //then
    }

}