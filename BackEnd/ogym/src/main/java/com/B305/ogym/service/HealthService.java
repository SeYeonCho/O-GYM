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
    private final UserService userService;
    public HealthDto.MyStudentsHealthListResponse findMyStudentsHealth(){
        String teacherEmail = userService.getMyUserWithAuthorities().getEmail();
        return ptTeacherRepository.findMyStudentsHealth(teacherEmail);
    }
}
