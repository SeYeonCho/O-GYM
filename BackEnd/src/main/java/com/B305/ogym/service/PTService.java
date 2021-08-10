package com.B305.ogym.service;

import com.B305.ogym.controller.dto.PTDto.reservationRequest;
import com.B305.ogym.domain.mappingTable.PTStudentPTTeacher;
import com.B305.ogym.domain.mappingTable.PTStudentPTTeacherRepository;
import com.B305.ogym.domain.users.common.UserBase;
import com.B305.ogym.domain.users.ptStudent.PTStudent;
import com.B305.ogym.domain.users.ptStudent.PTStudentRepository;
import com.B305.ogym.domain.users.ptTeacher.PTTeacher;
import com.B305.ogym.domain.users.ptTeacher.PTTeacherRepository;
import com.B305.ogym.exception.user.UnauthorizedException;
import com.B305.ogym.exception.user.UserNotFoundException;
import com.sun.jdi.request.DuplicateRequestException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PTService {

    private final PTTeacherRepository ptTeacherRepository;
    private final PTStudentRepository ptStudentRepository;
    private final PTStudentPTTeacherRepository ptStudentPTTeacherRepository;

    // 예약 생성
    @Transactional
    public void makeReservation(UserBase user, reservationRequest request) {
        if (!user.getRole().equals("ROLE_PTSTUDENT")) {
            throw new UnauthorizedException("선생님은 예약불가능");
        }

        String studentEmail = user.getEmail();
        String teacherEmail = request.getPtTeacherEmail();
        LocalDateTime time = request.getReservationTime();

        PTTeacher ptTeacher = ptTeacherRepository.findByEmail(teacherEmail)
            .orElseThrow(() -> new UserNotFoundException("TEACHER"));

        PTStudent ptStudent = ptStudentRepository.findByEmail(studentEmail)
            .orElseThrow(() -> new UserNotFoundException("해당하는 이메일은 존재하지 않습니다."));

        try {
            PTStudentPTTeacher ptStudentPTTeacher = ptStudent
                .makeReservation(ptTeacher, ptStudent, time);
            ptStudentPTTeacherRepository.save(ptStudentPTTeacher);
        } catch (RuntimeException ex) {
            throw new DuplicateRequestException("중복된 예약");
        }
    }

    @Transactional
    public void cancleReservation(String ptStudentEmail, reservationRequest request) {

        // 로그인한 사용자 찾을 수 없음
        PTStudent ptStudent = ptStudentRepository.findByEmail(ptStudentEmail)
            .orElseThrow(() -> new UnauthorizedException("로그인한 사용자 없음"));

        // 선생님 정보 찾을 수 없음
        PTTeacher ptTeacher = ptTeacherRepository.findByEmail(request.getPtTeacherEmail())
            .orElseThrow(() -> new UserNotFoundException("CANCLE_RESERVATION"));

        // 예약정보 찾을 수 없음
        PTStudentPTTeacher ptStudentPTTeacher = ptStudentPTTeacherRepository
            .findByPtTeacherAndPtStudentAndReservationDate(ptTeacher, ptStudent,
                request.getReservationTime())
            .orElseThrow(() -> new UserNotFoundException("CANCLE_RESERVATION"));

        ptStudentPTTeacherRepository.delete(ptStudentPTTeacher);

    }
}
