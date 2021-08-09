package com.B305.ogym.service;

import com.B305.ogym.controller.dto.PTDto;
import com.B305.ogym.controller.dto.PTDto.CancelReservationRequest;
import com.B305.ogym.domain.mappingTable.PTStudentPTTeacher;
import com.B305.ogym.domain.mappingTable.PTStudentPTTeacherRepository;
import com.B305.ogym.domain.users.UserRepository;
import com.B305.ogym.domain.users.common.UserBase;
import com.B305.ogym.domain.users.ptStudent.PTStudent;
import com.B305.ogym.domain.users.ptStudent.PTStudentRepository;
import com.B305.ogym.domain.users.ptTeacher.PTTeacher;
import com.B305.ogym.domain.users.ptTeacher.PTTeacherRepository;
import com.B305.ogym.exception.user.ReservationDuplicateException;
import com.B305.ogym.exception.user.UnauthorizedException;
import com.B305.ogym.exception.user.UserNotFoundException;
import com.sun.jdi.request.DuplicateRequestException;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PTService {

    private final PTTeacherRepository ptTeacherRepository;
    private final PTStudentRepository ptStudentRepository;
    private final PTStudentPTTeacherRepository ptStudentPTTeacherRepository;

    // 예약 생성
    @Transactional
    public void makeReservation(UserBase user, PTDto.SaveReservationRequest request) {
        if (!user.getRole().equals("ROLE_PTSTUDENT")) {
            throw new UnauthorizedException("선생님은 예약불가능");
        }

        String studentEmail = user.getEmail();
        String teacherEmail = request.getPtTeacherEmail();
        LocalDateTime time = request.getReservationTime();

        PTTeacher ptTeacher = ptTeacherRepository.findByEmail(teacherEmail);
        if (ptTeacher == null) {
            throw new UserNotFoundException("TEACHER");
        }
        PTStudent ptStudent = ptStudentRepository.findByEmail(studentEmail);

        try {
            PTStudentPTTeacher ptStudentPTTeacher = ptStudent
                .makeReservation(ptTeacher, ptStudent, time);
            ptStudentPTTeacherRepository.save(ptStudentPTTeacher);
        } catch (RuntimeException ex) {
            throw new DuplicateRequestException("중복된 예약");
        }
    }

    @Transactional
    public void cancleReservation(UserBase user, CancelReservationRequest request) {
        PTStudentPTTeacher ptStudentPTTeacher = ptStudentPTTeacherRepository
            .findById(Long.valueOf(request.getReservationId()))
            .orElseThrow(() -> new UserNotFoundException("CANCLE_RESERVATION"));

        // 로그인한 사용자의 선생님/학생 역할을 고려하여 적절한 비교 ID를 thisId에 저장
        Long thisId;
        switch (user.getRole()) {
            case "ROLE_PTSTUDENT":
                thisId = ptStudentPTTeacher.getPtStudent().getId();
                break;
            case "ROLE_PTTEACHER":
                thisId = ptStudentPTTeacher.getPtTeacher().getId();
                break;
            default:
                throw new UnauthorizedException("허용되지 않는 기능입니다.");
        }

        if (thisId != user.getId()) {
            // 삭제하려는 예약이 로그인한 사용자의 것이 아닌 경우
            throw new UnauthorizedException("CANCLE_RESERVATION");
        } else {
            ptStudentPTTeacherRepository.delete(ptStudentPTTeacher);
        }

    }
}
