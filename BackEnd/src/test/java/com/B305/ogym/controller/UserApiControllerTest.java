package com.B305.ogym.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import com.B305.ogym.common.annotation.WithAuthUser;
import com.B305.ogym.common.config.SecurityConfig;
import com.B305.ogym.controller.dto.UserDto.SaveUserRequest;
import com.B305.ogym.exception.user.UserDuplicateEmailException;
import com.B305.ogym.exception.user.UserDuplicateNicknameException;
import com.B305.ogym.exception.user.UserNotFoundException;
import com.B305.ogym.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
//@AutoConfigureMockMvc(addFilters = false)
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


    private SaveUserRequest createTeacherRequest() {
        return SaveUserRequest.builder()
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
            .description("트레이너")
            .build();
    }

    private SaveUserRequest createStudentRequest() {
        return SaveUserRequest.builder()
            .email("hello@naver.com")
            .password("asdasd")
            .username("한솥")
            .nickname("nononoo1")
            .gender(0)
            .tel("010-0000-0000")
            .zipCode("12345")
            .street("도로명")
            .detailedAddress("상세주소")
            .role("ROLE_PTSTUDENT")
            .monthlyWeights(new ArrayList<>(Arrays
                .asList(180, 200, 210, 180, 200, 210, 180, 200, 210, 180, 200, 210)))
            .monthlyHeights(new ArrayList<>(Arrays
                .asList(180, 200, 210, 180, 200, 210, 180, 200, 210, 180, 200, 210)))
            .build();
    }

    @DisplayName("선생님 회원가입 - 모든 유효성 검사에 통과했다면 회원가입 성공")
    @Test
    public void createTeacher_success() throws Exception {
        //given
        SaveUserRequest teacherRequest = createTeacherRequest();

        //when
        doNothing().when(userService).signup(teacherRequest);

        //then
        mockMvc.perform(post("/api/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(teacherRequest)))
            .andDo(print())
            .andExpect(status().isCreated()) // 201 isCreated()
            .andDo(
                document(
                    "userApi/signup/teacher/successful",
                    requestFields(
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
                        fieldWithPath("snsAddrs").type(JsonFieldType.NULL)
                            .description("The user's snsAddrs")
                    ).and(
                        fieldWithPath("monthlyHeights").type(JsonFieldType.NULL)
                            .description("트레이너의 경우 입력받지 않습니다."),
                        fieldWithPath("monthlyWeights").type(JsonFieldType.NULL)
                            .description("트레이너의 경우 입력받지 않습니다.")
                    )));
    }

    @DisplayName("학생 회원가입 - 모든 유효성 검사에 통과했다면 회원가입 성공")
    @Test
    public void createStudent_success() throws Exception {
        //given
        SaveUserRequest studentRequest = createStudentRequest();
        //when
        doNothing().when(userService).signup(studentRequest);

        //then
        mockMvc.perform(post("/api/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(studentRequest)))
            .andDo(print())
            .andExpect(status().isCreated()) // 201 isCreated()
            .andDo(document("userApi/signup/student/successful", requestFields(
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
                fieldWithPath("monthlyHeights").type(JsonFieldType.ARRAY)
                    .description("The user's monthlyHeights"),
                fieldWithPath("monthlyWeights").type(JsonFieldType.ARRAY)
                    .description("The user's monthlyWeights"))
                .and(
                    fieldWithPath("major").type(JsonFieldType.STRING)
                        .description("학생의 경우 입력받지 않습니다").optional(),
                    fieldWithPath("certificates").type(JsonFieldType.ARRAY)
                        .description("학생의 경우 입력받지 않습니다").optional(),
                    fieldWithPath("careers").type(JsonFieldType.ARRAY)
                        .description("학생의 경우 입력받지 않습니다").optional(),
                    fieldWithPath("price").type(JsonFieldType.NUMBER)
                        .description("학생의 경우 입력받지 않습니다").optional(),
                    fieldWithPath("description").type(JsonFieldType.STRING)
                        .description("학생의 경우 입력받지 않습니다").optional(),
                    fieldWithPath("snsAddrs").type(JsonFieldType.NULL)
                        .description("학생의 경우 입력받지 않습니다").optional()
                )));
    }

    @DisplayName("유저 회원가입 - 이메일 중복으로 인한 회원가입 실패")
    @Test
    public void createUser_email_failure() throws Exception {
        //given
        SaveUserRequest teacherRequest = createTeacherRequest();

        //when
        doThrow(new UserDuplicateEmailException("이메일 중복")).when(userService).signup(any());
        //then
        mockMvc.perform(post("/api/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(teacherRequest)))
            .andDo(print())
            .andExpect(status().isConflict())
            .andDo(document("userApi/signup/duplicate/email"
            ));
    }

    @DisplayName("유저 회원가입 - 닉네임 중복으로 인한 회원가입 실패")
    @Test
    public void createUser_nickName_failure() throws Exception {
        //given
        SaveUserRequest teacherRequest = createTeacherRequest();

        //when
        doThrow(new UserDuplicateNicknameException("이메일 중복")).when(userService).signup(any());
        //then
        mockMvc.perform(post("/api/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(teacherRequest)))
            .andDo(print())
            .andExpect(status().isConflict())
            .andDo(document("userApi/signup/duplicate/nickname"
            ));
    }

    @WithAuthUser(email = "teacher@naver.com", role = "ROLE_PTTEACHER")
    @DisplayName("선생님 회원탈퇴 - 회원탈퇴 성공")
    @Test
    public void deleteTeacher_success() throws Exception {
        //given
        String email = "teacher@naver.com";

        //when
        doNothing().when(userService).deleteUserBase(email);

        //then
        mockMvc.perform(delete("/api/user"))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("userApi/delete/teacher/successful"
            ));
    }

    @WithAuthUser(email = "teacher@naver.com", role = "ROLE_PTSTUDENT")
    @DisplayName("학생 회원탈퇴 - 존재하지 않는 이메일로 인한 회원탈퇴 실패(이미 탈퇴한 회원)")
    @Test
    public void deleteTeacher_failure() throws Exception {
        //given
        String email = "teacher@naver.com";

        //when
        doThrow(new UserNotFoundException("해당하는 이메일이 존재하지 않습니다")).when(userService)
            .deleteUserBase(email);

        //then
        mockMvc.perform(delete("/api/user"))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andDo(document("userApi/delete/teacher/failure"
            ));
    }

    @WithAuthUser(email = "student@naver.com", role = "ROLE_PTSTUDENT")
    @DisplayName("학생 회원탈퇴 - 회원탈퇴 성공")
    @Test
    public void deleteStudent_success() throws Exception {
        //given
        String email = "student@naver.com";

        //when
        doNothing().when(userService).deleteUserBase(email);

        //then
        mockMvc.perform(delete("/api/user"))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("userApi/delete/student/successful"
            ));
    }

    @WithAuthUser(email = "student@naver.com", role = "ROLE_PTSTUDENT")
    @DisplayName("학생 회원탈퇴 - 존재하지 않는 이메일로 인한 회원탈퇴 실패(이미 탈퇴한 회원)")
    @Test
    public void deleteStudent_failure() throws Exception {
        //given
        String email = "student@naver.com";

        //when
        doThrow(new UserNotFoundException("해당하는 이메일이 존재하지 않습니다")).when(userService)
            .deleteUserBase(email);

        //then
        mockMvc.perform(delete("/api/user"))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andDo(document("userApi/delete/student/failure"
            ));
    }

    @WithAuthUser(email = "teacher@naver.com", role = "ROLE_PTTEACHER")
    @DisplayName("회원 정보조회 - 정보조회 성공")
    @Test
    public void getUserInfo_success() throws Exception {
        String email = "teacher@naver.com";
        Map<String, Object> data = new HashMap<>();
        data.put("id", 1);
        data.put("email", "teacher@naver.com");

        ArrayList<String> req = new ArrayList<>(Arrays.asList("id", "email"));
        Map<String, Object> reqData = new HashMap<>();
        reqData.put("req", req);

        given(userService.getUserInfo(email, req)).willReturn(data);

        mockMvc.perform(get("/api/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reqData)))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("userApi/getUserInfo/successful"));
    }


}
