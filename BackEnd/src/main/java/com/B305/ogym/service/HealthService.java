package com.B305.ogym.service;

import com.B305.ogym.controller.dto.HealthDto;
import com.B305.ogym.controller.dto.HealthDto.MyHealthResponse;
import com.B305.ogym.controller.dto.HealthDto.SetMyHealthRequest;
import com.B305.ogym.domain.users.UserRepository;
import com.B305.ogym.domain.users.common.UserBase;
import com.B305.ogym.domain.users.ptStudent.Monthly;
import com.B305.ogym.domain.users.ptStudent.MonthlyRepository;
import com.B305.ogym.domain.users.ptStudent.PTStudent;
import com.B305.ogym.domain.users.ptStudent.PTStudentRepository;
import com.B305.ogym.domain.users.ptTeacher.PTTeacherRepository;
import com.B305.ogym.exception.health.HealthDuplicateException;
import com.B305.ogym.exception.user.UnauthorizedException;
import com.B305.ogym.exception.user.UserNotFoundException;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HealthService {

    private final PTTeacherRepository ptTeacherRepository;
    private final PTStudentRepository ptStudentRepository;
    private final MonthlyRepository monthlyRepository;
    private final UserRepository userRepository;

    // 로그인한 사용자의 건강정보 조회
    @Transactional
    public MyHealthResponse getMyHealth(String userEmail) {
//        UserBase user = userRepository.findByEmail(userEmail)
//            .orElseThrow(() -> new UserNotFoundException("해당하는 이메일이 존재하지 않습니다."));
//        if (!user.getRole().equals("ROLE_PTSTUDENT")) {
//            throw new UnauthorizedException("허용되지 않은 접근입니다.");
//        }
        PTStudent ptStudent = ptStudentRepository.findByEmail(userEmail)
            .orElseThrow(() -> new UserNotFoundException("해당하는 이메일이 존재하지 않습니다."));

        MyHealthResponse myHealthResponse = ptStudent.getMyHealthResponse(ptStudent);

        return myHealthResponse;
    }


    public HealthDto.MyStudentsHealthListResponse findMyStudentsHealth(String teacherEmail) {
        return ptTeacherRepository.findMyStudentsHealth(teacherEmail);
    }
}
