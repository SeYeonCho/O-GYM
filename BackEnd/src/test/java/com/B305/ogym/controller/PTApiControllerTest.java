package com.B305.ogym.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import com.B305.ogym.common.annotation.WithAuthUser;
import com.B305.ogym.common.config.SecurityConfig;
import com.B305.ogym.controller.dto.PTDto.AllTeacherListResponse;
import com.B305.ogym.controller.dto.PTDto.PTTeacherDto;
import com.B305.ogym.controller.dto.PTDto.reservationRequest;
import com.B305.ogym.exception.user.UserNotFoundException;
import com.B305.ogym.service.PTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
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
@WebMvcTest(controllers = PTApiController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}
)
//@ActiveProfiles("test") // 테스트에서 사용할 profile
@MockBean(JpaMetamodelMappingContext.class) // @EnableJPaAuditing 사용시 추가해야하는 어노테이션
//@AutoConfigureMockMvc(addFilters = false)
class PTApiControllerTest {

    @MockBean
    private PTService ptService;

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


    @WithAuthUser(email = "student@naver.com", role = "ROLE_PTSTUDENT")
    @DisplayName("선생님 리스트 불러오기 - 성공")
    @Test
    public void getTeacherList_Success() throws Exception {
        AllTeacherListResponse allTeacherListResponse = AllTeacherListResponse.builder()
            .teacherList(new ArrayList<PTTeacherDto>())
            .build();
        given(ptService.getTeacherList()).willReturn(allTeacherListResponse);

        mockMvc.perform(get("/api/pt/teacherlist"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @WithAuthUser(email = "student@naver.com", role = "ROLE_PTSTUDENT")
    @DisplayName("PT 예약하기 - 성공")
    @Test
    public void makeReservation_Success() throws Exception {
        reservationRequest req = reservationRequest.builder()
            .ptTeacherEmail("teacher@naver.com")
            .build();

        doNothing().when(ptService).makeReservation(any(), any());

        mockMvc.perform(post("/api/pt/reservation")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @WithAuthUser(email = "student@naver.com", role = "ROLE_PTSTUDENT")
    @DisplayName("PT 예약하기 - 해당하는 선생님 이메일이 존재하지 않아 실패")
    @Test
    public void makeReservation_teacherNotFound() throws Exception {
        reservationRequest req = reservationRequest.builder()
            .ptTeacherEmail("teacher@naver.com")
            .build();

        doThrow(new UserNotFoundException("존재하지 않는 트레이너입니다.")).when(ptService)
            .makeReservation(any(), any());

        mockMvc.perform(post("/api/pt/reservation")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @WithAuthUser(email = "student@naver.com", role = "ROLE_PTSTUDENT")
    @DisplayName("PT 예약하기 - 해당하는 학생 이메일이 존재하지 않아 실패")
    @Test
    public void makeReservation_studentNotFound() throws Exception {
        reservationRequest req = reservationRequest.builder()
            .ptTeacherEmail("teacher@naver.com")
            .build();

        doThrow(new UserNotFoundException("존재하지 않는 트레이너입니다.")).when(ptService)
            .makeReservation(any(), any());

        mockMvc.perform(post("/api/pt/reservation")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @WithAuthUser(email = "student@naver.com", role = "ROLE_PTSTUDENT")
    @DisplayName("PT 예약 취소하기 - 성공")
    @Test
    public void cancelReservation_Success() throws Exception {
        reservationRequest req = reservationRequest.builder()
            .ptTeacherEmail("teacher@naver.com")
            .build();

        doNothing().when(ptService).cancelReservation(eq("student@naver.com"), eq(req));

        mockMvc.perform(delete("/api/pt/reservation")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @WithAuthUser(email = "student@naver.com", role = "ROLE_PTSTUDENT")
    @DisplayName("PT 예약 취소하기 - 요청한 학생이 존재하지 않아 실패")
    @Test
    public void cancelReservation_studentNotFound() throws Exception {
        reservationRequest req = reservationRequest.builder()
            .ptTeacherEmail("teacher@naver.com")
            .build();

        doThrow(new UserNotFoundException("존재하지 않는 학생입니다.")).when(ptService)
            .cancelReservation(any(), any());

        mockMvc.perform(delete("/api/pt/reservation")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @WithAuthUser(email = "student@naver.com", role = "ROLE_PTSTUDENT")
    @DisplayName("PT 예약 취소하기 - 요청한 트레이너가 존재하지 않아 실패")
    @Test
    public void cancelReservation_teacherNotFound() throws Exception {
        reservationRequest req = reservationRequest.builder()
            .ptTeacherEmail("teacher@naver.com")
            .build();

        doThrow(new UserNotFoundException("존재하지 않는 트레이너입니다.")).when(ptService)
            .cancelReservation(any(), any());

        mockMvc.perform(delete("/api/pt/reservation")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @WithAuthUser(email = "student@naver.com", role = "ROLE_PTSTUDENT")
    @DisplayName("PT 예약 취소하기 - 제거 요청한 예약이 존재하지 않아 실패")
    @Test
    public void cancelReservation_reservationNotFound() throws Exception {
        reservationRequest req = reservationRequest.builder()
            .ptTeacherEmail("teacher@naver.com")
            .build();

        doThrow(new UserNotFoundException("존재하지 않는 예약입니다.")).when(ptService)
            .cancelReservation(any(), any());

        mockMvc.perform(delete("/api/pt/reservation")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

}