package com.B305.ogym.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.B305.ogym.controller.dto.UserDto.SaveUserRequest;
import com.B305.ogym.domain.authority.AuthorityRepository;
import com.B305.ogym.domain.users.UserRepository;
import com.B305.ogym.domain.users.ptStudent.PTStudent;
import com.B305.ogym.domain.users.ptStudent.PTStudentRepository;
import com.B305.ogym.domain.users.ptTeacher.PTTeacher;
import com.B305.ogym.domain.users.ptTeacher.PTTeacherRepository;
import com.B305.ogym.exception.user.UserDuplicateEmailException;
import com.B305.ogym.exception.user.UserDuplicateNicknameException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
            .role("ROLE_PTTEACHER")
            .monthlyWeights(new ArrayList<Integer>(Arrays
                .asList(180, 200, 210, 180, 200, 210, 180, 200, 210, 180, 200, 210)))
            .monthlyHeights(new ArrayList<Integer>(Arrays
                .asList(180, 200, 210, 180, 200, 210, 180, 200, 210, 180, 200, 210)))
            .build();
    }

    private PTStudent createStudent() {
        return createStudentRequest().toPTStudentEntity();
    }

    private PTTeacher createTeacher() {
        return createTeacherRequest().toPTTeacherEntity();
    }

    @DisplayName("이메일 중복 시 회원가입 실패")
    @Test
    public void signUp_emailDuplicate() throws Exception {
        //given
        SaveUserRequest studentRequest = createStudentRequest();
        //when
        when(userRepository.existsByEmail("hello@naver.com")).thenReturn(true);
        //then
        assertThrows(UserDuplicateEmailException.class, () -> userService.signup(studentRequest));

        verify(userRepository, atLeastOnce()).existsByEmail("hello@naver.com");
    }

    @DisplayName("닉네임 중복 시 회원가입 실패")
    @Test
    public void signUp_nickNameDuplicate() throws Exception {
        //given
        SaveUserRequest studentRequest = createStudentRequest();
        //when
        when(userRepository.existsByNickname("nononoo1")).thenReturn(true);
        //then
        assertThrows(UserDuplicateNicknameException.class,
            () -> userService.signup(studentRequest));

        verify(userRepository, atLeastOnce()).existsByNickname("nononoo1");
    }

    @DisplayName("학생 회원 탈퇴 성공")
    @Test
    public void deleteStudent_success() throws Exception {
        //given
        var user = createStudent();
        String email = user.getEmail();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        //when
        userService.deleteUserBase(user.getEmail(), email);

        //then
        verify(userRepository, atLeastOnce()).findByEmail(email);
    }

    @DisplayName("트레이너 회원 탈퇴 성공")
    @Test
    public void deleteTeacher_success() throws Exception {
        //given
        var user = createTeacher();
        String email = user.getEmail();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        //when
        userService.deleteUserBase(user.getEmail(), email);

        //then
        verify(userRepository, atLeastOnce()).findByEmail(email);
    }


}