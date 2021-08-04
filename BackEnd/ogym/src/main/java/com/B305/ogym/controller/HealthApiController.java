package com.B305.ogym.controller;

import com.B305.ogym.controller.dto.HealthDto;
import com.B305.ogym.controller.dto.SuccessResponseDto;
import com.B305.ogym.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthApiController {
    private final HealthService healthService;

    @GetMapping("/mystudents/{teacherId}")
    public ResponseEntity<SuccessResponseDto> getMyStudentsHealth(@PathVariable(value = "teacherId") long teacherId){
        return ResponseEntity.ok(new SuccessResponseDto<HealthDto.MyStudentsHealthListResponse>(
            200, "건강정보 조회에 성공했습니다.", healthService.findMyStudentsHealth(teacherId)
        ));
    }

    @GetMapping("/myhealth")
    public ResponseEntity<SuccessResponseDto> getMyHealth(){
        return ResponseEntity.ok(new SuccessResponseDto<HealthDto.GetMyHealthResponse>(
            200, "건강정보 조회에 성공했습니다.", healthService.getMyHealthResponse()
        ));
    }
}