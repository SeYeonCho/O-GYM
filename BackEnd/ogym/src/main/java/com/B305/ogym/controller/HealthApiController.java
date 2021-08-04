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

    @GetMapping("/mystudents")
    public ResponseEntity<SuccessResponseDto> getMyStudentsHealth(){
        return ResponseEntity.ok(new SuccessResponseDto<HealthDto.MyStudentsHealthListResponse>(
            200, "건강정보 조회에 성공했습니다.", healthService.findMyStudentsHealth()
        ));
    }
}
