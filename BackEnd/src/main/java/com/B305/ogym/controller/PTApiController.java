package com.B305.ogym.controller;

import com.B305.ogym.controller.dto.PTDto;
import com.B305.ogym.controller.dto.SuccessResponseDto;
import com.B305.ogym.domain.users.common.UserBase;
import com.B305.ogym.service.PTService;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    // 선생님 리스트 출력
    @GetMapping("/teacherlist")
    @PreAuthorize("hasAnyRole('PTSTUDENT', 'PTTEACHER', 'USER')")
    public ResponseEntity<SuccessResponseDto> teacherList() {

        return ResponseEntity.ok(new SuccessResponseDto<PTDto.AllTeacherListResponse>(200,
            "PT 선생님 리스트 불러오기에 성공하였습니다.", ptService.getTeacherList()));
    }

    // PT 예약 생성
    @PostMapping("/reservation")
    @PreAuthorize("hasAnyRole('PTSTUDENT')")
    public ResponseEntity<SuccessResponseDto> makeReservation(
        @AuthenticationPrincipal UserBase user,
        @RequestBody @Valid PTDto.reservationRequest request) {

        System.out.println("Controller - useremail:" + user.getEmail());

        ptService.makeReservation(user, request);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new SuccessResponseDto<Map>(201, "PT예약에 성공하였습니다.", new HashMap()));
    }

    @DeleteMapping("/reservation")
    @PreAuthorize("hasAnyRole('PTSTUDENT')")
    public ResponseEntity<SuccessResponseDto> cancleReservation(
        @AuthenticationPrincipal UserBase user,
        @RequestBody @Valid PTDto.reservationRequest request
    ) {
        System.out.println("controller" + user.getRole());
        ptService.cancleReservation(user.getEmail(), request);
        return ResponseEntity.ok(new SuccessResponseDto<Map>(
            200, "PT 삭제에 성공했습니다.", new HashMap()
        ));
    }
}
