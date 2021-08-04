package com.B305.ogym.service;

import com.B305.ogym.controller.dto.HealthDto;
import com.B305.ogym.domain.users.ptTeacher.PTTeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HealthService {

    private final PTTeacherRepository ptTeacherRepository;
    private final PTStudentRepository ptStudentRepository;


    // 로그인한 사용자의 건강정보 조회
    @Transactional
    public HealthDto.GetMyHealthResponse getMyHealthResponse() {

        ////
        // Token으로부터 claim 걸어서 사용자 정보 가져와야함 (이메일)
        ////

        String email = "chuu@ssafy.com";
        PTStudent ptStudent = ptStudentRepository.findByEmail(email);

        GetMyHealthResponse myHealthResponse = ptStudent.getMyHealthResponse(ptStudent);

        return myHealthResponse;
    }

    private final UserService userService;

    public HealthDto.MyStudentsHealthListResponse findMyStudentsHealth() {
        String teacherEmail = userService.getMyUserWithAuthorities().getEmail();
        return ptTeacherRepository.findMyStudentsHealth(teacherEmail);
    }
}
