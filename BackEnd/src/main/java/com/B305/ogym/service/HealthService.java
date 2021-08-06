package com.B305.ogym.service;

import com.B305.ogym.controller.dto.HealthDto;
import com.B305.ogym.controller.dto.HealthDto.GetMyHealthResponse;
import com.B305.ogym.domain.users.common.UserBase;
import com.B305.ogym.domain.users.ptStudent.PTStudent;
import com.B305.ogym.domain.users.ptStudent.PTStudentRepository;
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
    public HealthDto.GetMyHealthResponse getMyHealthResponse(String email) {

        System.out.println("email: " +  email);

        PTStudent ptStudent = ptStudentRepository.findByEmail(email);

        GetMyHealthResponse myHealthResponse = ptStudent.getMyHealthResponse(ptStudent);

        return myHealthResponse;
    }


    public HealthDto.MyStudentsHealthListResponse findMyStudentsHealth(Long teacherId) {
        return ptTeacherRepository.findMyStudentsHealth(teacherId);
    }
}
