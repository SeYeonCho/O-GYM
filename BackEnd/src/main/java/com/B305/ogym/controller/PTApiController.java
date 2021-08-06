package com.B305.ogym.controller;

import com.B305.ogym.controller.dto.PTDto;
import com.B305.ogym.controller.dto.SuccessResponseDto;
import com.B305.ogym.controller.dto.UserDto;
import com.B305.ogym.domain.users.common.UserBase;
import com.B305.ogym.service.PTService;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pt")
@RequiredArgsConstructor
public class PTApiController {
    private final PTService ptService;

    // PT 예약 생성
    @PostMapping ("/makeReservation")
    public ResponseEntity<SuccessResponseDto> makeReservation(
        @AuthenticationPrincipal UserBase user,
        @RequestBody @Valid PTDto.SaveReservationRequest request){

        System.out.println("Controller - useremail:" + user.getEmail());

        ptService.makeReservation(user.getEmail(), request);

        return ResponseEntity.ok(new SuccessResponseDto<Map>(
            201, "PT 예약이 정상적으로 이루어졌습니다.", new HashMap()
        ));
    }
}
