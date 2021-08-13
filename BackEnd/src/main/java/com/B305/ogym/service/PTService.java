package com.B305.ogym.service;

import com.B305.ogym.controller.dto.PTDto.AllTeacherListResponse;
import com.B305.ogym.controller.dto.PTDto.PTTeacherDto;
import com.B305.ogym.controller.dto.PTDto.SearchDto;
import com.B305.ogym.controller.dto.PTDto.reservationRequest;
import com.B305.ogym.domain.mappingTable.PTStudentPTTeacher;
import com.B305.ogym.domain.mappingTable.PTStudentPTTeacherRepository;
import com.B305.ogym.domain.users.ptStudent.PTStudent;
import com.B305.ogym.domain.users.ptStudent.PTStudentRepository;
import com.B305.ogym.domain.users.ptTeacher.PTTeacher;
import com.B305.ogym.domain.users.ptTeacher.PTTeacherRepository;
import com.B305.ogym.exception.user.UserNotFoundException;
import com.sun.jdi.request.DuplicateRequestException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public void makeReservation(String ptStudentEmail, reservationRequest request) {

        String ptTeacherEmail = request.getPtTeacherEmail();
        LocalDateTime time = request.getReservationTime();

        // 선생님 정보 찾을 수 없음
        PTTeacher ptTeacher = ptTeacherRepository.findByEmail(ptTeacherEmail)
            .orElseThrow(() -> new UserNotFoundException("TEACHER"));

        // 학생 정보 찾을 수 없음
        PTStudent ptStudent = ptStudentRepository.findByEmail(ptStudentEmail)
            .orElseThrow(() -> new UserNotFoundException("해당하는 이메일은 존재하지 않습니다."));

        // 해당 시간에 예약이 이미 존재함
        try {
            PTStudentPTTeacher ptStudentPTTeacher = ptStudent
                .makeReservation(ptTeacher, ptStudent, time);
            ptStudentPTTeacherRepository.save(ptStudentPTTeacher);
        } catch (RuntimeException ex) {
            throw new DuplicateRequestException("중복된 예약");
        }
    }

    @Transactional
    public void cancelReservation(String ptStudentEmail, reservationRequest request) {

        // 로그인한 사용자 찾을 수 없음
        PTStudent ptStudent = ptStudentRepository.findByEmail(ptStudentEmail)
            .orElseThrow(() -> new UserNotFoundException("로그인한 사용자 없음"));

        // 선생님 정보 찾을 수 없음
        PTTeacher ptTeacher = ptTeacherRepository.findByEmail(request.getPtTeacherEmail())
            .orElseThrow(() -> new UserNotFoundException("CANCEL_RESERVATION"));

        // 예약정보 찾을 수 없음
        PTStudentPTTeacher ptStudentPTTeacher = ptStudentPTTeacherRepository
            .findByPtTeacherAndPtStudentAndReservationDate(ptTeacher, ptStudent,
                request.getReservationTime())
            .orElseThrow(() -> new UserNotFoundException("CANCEL_RESERVATION"));

        ptStudentPTTeacherRepository.delete(ptStudentPTTeacher);

    }

    // 선생님 리스트 출력
    @Transactional
    public AllTeacherListResponse getTeacherList(SearchDto searchDto, Pageable pageable) {

        // 조건 검색
        Page<PTTeacher> ptTeachers = ptTeacherRepository.searchAll(searchDto, pageable);

        // PTTeacher 에서 원하는 정보만 담아 PTTeacherDto로 변환
        List<PTTeacherDto> ptTeacherDtos = new ArrayList<>();
        for (int i = 0; i < ptTeachers.getNumberOfElements(); i++) {
            PTTeacher ptTeacher = ptTeachers.getContent().get(i);
            ptTeacherDtos.add(ptTeacher.toPTTeacherDto());
        }

        // Content와 Paging 정보를 함께 Response 객체에 담아 반환
        AllTeacherListResponse allTeacherListResponse = AllTeacherListResponse.builder()
            .teacherList(ptTeacherDtos)
            .pageable(ptTeachers.getPageable())
            .totalPages(ptTeachers.getTotalPages())
            .totalElements(ptTeachers.getTotalElements())
            .numberOfElements(ptTeachers.getNumberOfElements())
            .build();

        return allTeacherListResponse;
    }

    public List<LocalDateTime> getTeacherReservationTime(String teacherEmail) {
        return ptTeacherRepository.reservationTime(teacherEmail);
    }
}
