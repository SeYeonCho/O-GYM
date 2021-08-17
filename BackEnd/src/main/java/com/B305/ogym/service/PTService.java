package com.B305.ogym.service;

import com.B305.ogym.controller.dto.PTDto.AllTeacherListResponse;
import com.B305.ogym.controller.dto.PTDto.PTTeacherDto;
import com.B305.ogym.controller.dto.PTDto.SearchDto;
import com.B305.ogym.controller.dto.PTDto.nowReservationDto;
import com.B305.ogym.controller.dto.PTDto.reservationDto;
import com.B305.ogym.controller.dto.PTDto.reservationRequest;
import com.B305.ogym.domain.mappingTable.PTStudentPTTeacher;
import com.B305.ogym.domain.mappingTable.PTStudentPTTeacherRepository;
import com.B305.ogym.domain.users.UserRepository;
import com.B305.ogym.domain.users.common.UserBase;
import com.B305.ogym.domain.users.ptStudent.PTStudent;
import com.B305.ogym.domain.users.ptStudent.PTStudentRepository;
import com.B305.ogym.domain.users.ptTeacher.PTTeacher;
import com.B305.ogym.domain.users.ptTeacher.PTTeacherRepository;
import com.B305.ogym.exception.pt.ReservationNotFoundException;
import com.B305.ogym.exception.user.UserNotFoundException;
import com.sun.jdi.request.DuplicateRequestException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final UserRepository userRepository;

    // 예약 생성
    @Transactional
    public void makeReservation(String ptStudentEmail, reservationRequest request) {

        String ptTeacherEmail = request.getPtTeacherEmail();
        LocalDateTime time = request.getReservationTime();
        String description = request.getDescription();
        // 선생님 정보 찾을 수 없음
        PTTeacher ptTeacher = ptTeacherRepository.findByEmail(ptTeacherEmail)
            .orElseThrow(() -> new UserNotFoundException("TEACHER"));

        // 학생 정보 찾을 수 없음
        PTStudent ptStudent = ptStudentRepository.findByEmail(ptStudentEmail)
            .orElseThrow(() -> new UserNotFoundException("해당하는 이메일은 존재하지 않습니다."));

        // 해당 시간에 예약이 이미 존재함
        try {
            PTStudentPTTeacher ptStudentPTTeacher = ptStudent
                .makeReservation(ptTeacher, ptStudent, time, description);
            ptStudentPTTeacherRepository.save(ptStudentPTTeacher);
        } catch (RuntimeException ex) {
            throw new DuplicateRequestException("중복된 예약");
        }
    }

    // 예약 취소
    @Transactional
    public void cancelReservation(String ptStudentEmail, reservationRequest request) {

        // 로그인한 사용자 찾을 수 없음
        PTStudent ptStudent = ptStudentRepository.findByEmail(ptStudentEmail)
            .orElseThrow(() -> new UserNotFoundException("로그인한 사용자 없음"));

        // 선생님 정보 찾을 수 없음
        PTTeacher ptTeacher = ptTeacherRepository.findByEmail(request.getPtTeacherEmail())
            .orElseThrow(() -> new UserNotFoundException("이미 탈퇴한 선생님입니다."));

        // 예약정보 찾을 수 없음
        PTStudentPTTeacher ptStudentPTTeacher = ptStudentPTTeacherRepository
            .findByPtTeacherAndPtStudentAndReservationDate(ptTeacher, ptStudent,
                request.getReservationTime())
            .orElseThrow(() -> new ReservationNotFoundException("CANCEL_RESERVATION"));

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

        return AllTeacherListResponse.builder()
            .teacherList(ptTeacherDtos)
            .pageable(ptTeachers.getPageable())
            .totalPages(ptTeachers.getTotalPages())
            .totalElements(ptTeachers.getTotalElements())
            .numberOfElements(ptTeachers.getNumberOfElements())
            .build();
    }

    // 선생님의 PT 예약정보를 조회
    public List<LocalDateTime> getTeacherReservationTime(String teacherEmail) {
        if (!userRepository.existsByEmail(teacherEmail)) {
            throw new UserNotFoundException("해당 이메일이 존재하지 않습니다.");
        }
        return ptTeacherRepository.reservationTime(teacherEmail);
    }

    // 특정 유저의 PT 예약정보를 조회
    public List<reservationDto> getReservationTime(String email) {
        UserBase user = userRepository.findByEmail(email).orElseThrow(() ->
            new UserNotFoundException("해당하는 이메일이 존재하지 않습니다."));
        List<reservationDto> result = new ArrayList<>();
        if ("ROLE_PTTEACHER".equals(user.getAuthority().getAuthorityName())) {
            ptTeacherRepository.getReservationTime(email).forEach(
                o -> {
                    result.add(
                        reservationDto.builder()
                            .description(o.getDescription())
                            .reservationTime(o.getReservationDate())
                            .nickname(o.getPtStudent().getNickname())
                            .username(o.getPtStudent().getUsername())
                            .email(o.getPtStudent().getEmail())
                            .build()
                    );
                }
            );
        } else {
            ptStudentRepository.getReservationTime(email).forEach(
                o -> {
                    result.add(
                        reservationDto.builder()
                            .description(o.getDescription())
                            .reservationTime(o.getReservationDate())
                            .nickname(o.getPtTeacher().getNickname())
                            .username(o.getPtTeacher().getUsername())
                            .email(o.getPtTeacher().getEmail())
                            .build()
                    );
                }
            );
        }
        return result;
    }

    // 현재 예약정보를 조회
    public nowReservationDto getNowReservation(String userEmail) {
        String teacherEmail = null;
        String studentEmail = null;
        UserBase user = userRepository.findByEmail(userEmail).orElseThrow();
        if("ROLE_PTTEACHER".equals(user.getAuthority())){
            teacherEmail = userEmail;
        }else{
            studentEmail = userEmail;
        }
        List<String> nowReservation = ptStudentPTTeacherRepository
            .getNowReservation(teacherEmail, studentEmail, LocalDateTime.now());
        if (nowReservation == null) {
            return null;
        }
        nowReservationDto result = nowReservationDto.builder()
            .teacherNickname(nowReservation.get(0))
            .studentNickname(nowReservation.get(1))
            .build();
        return result;
    }


}
