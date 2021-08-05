package com.B305.ogym.service;

import static org.junit.jupiter.api.Assertions.*;

import com.B305.ogym.controller.dto.UserDto.SaveStudentRequest;
import com.B305.ogym.controller.dto.UserDto.SaveTeacherRequest;
import com.B305.ogym.domain.authority.AuthorityRepository;
import com.B305.ogym.domain.users.UserRepository;
import com.B305.ogym.domain.users.ptStudent.PTStudentRepository;
import com.B305.ogym.domain.users.ptTeacher.PTTeacherRepository;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PTTeacherRepository ptTeacherRepository;
    @Mock
    PTStudentRepository ptStudentRepository;
    @Mock
    AuthorityRepository authorityRepository;

    @InjectMocks
    UserService userService;

    private SaveTeacherRequest createTeacherRequest() {
        return SaveTeacherRequest.builder()
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
            .snsAddrs(new ArrayList<>())
            .build();
    }

    private SaveStudentRequest createStudentRequest() {
        return SaveStudentRequest.builder()
            .email("hello@naver.com")
            .password("asdasd")
            .username("한솥")
            .nickname("nononoo1")
            .gender(0)
            .tel("010-0000-0000")
            .zipCode("12345")
            .street("도로명")
            .detailedAddress("상세주소")
            .role("ROLE_PTTEACHER")
            .monthlyWeights(new ArrayList<Integer>(Arrays
                .asList(180, 200, 210, 180, 200, 210, 180, 200, 210, 180, 200, 210)))
            .monthlyHeights(new ArrayList<Integer>(Arrays
                .asList(180, 200, 210, 180, 200, 210, 180, 200, 210, 180, 200, 210)))
            .build();
    }


}