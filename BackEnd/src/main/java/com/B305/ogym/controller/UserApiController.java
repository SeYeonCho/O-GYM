package com.B305.ogym.controller;

import static com.B305.ogym.controller.dto.UserDto.SaveUserRequest;

import com.B305.ogym.controller.dto.SuccessResponseDto;
import com.B305.ogym.controller.dto.UserDto;
import com.B305.ogym.controller.dto.UserDto.GetUserInfoRequest;
import com.B305.ogym.domain.users.common.UserBase;
import com.B305.ogym.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserApiController {

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

    //    @PostMapping("/signup")
//    public ResponseEntity<SuccessResponseDto> signup(
//        @RequestBody @Valid SaveUserRequest signupRequest
//    ) {
//        userService.signup(signupRequest);
//        return ResponseEntity.ok(new SuccessResponseDto<Map>(
//            200, "회원가입이 성공했습니다", new HashMap()
//        ));
//    }
    // 사용자 회원 탈퇴
    @DeleteMapping("/user")
    @PreAuthorize("hasAnyRole('PTTEACHER','PTSTUDENT')")
    public ResponseEntity<SuccessResponseDto> deleteMyUser(
        @AuthenticationPrincipal UserBase user
    ) {
        userService.deleteUserBase(user.getEmail());
        // Security Context에서도 지워야한다.
        return ResponseEntity.ok(new SuccessResponseDto<Map>(
            200, "회원정보 삭제에 성공했습니다", new HashMap()
        ));
    }

//    // 학생 회원가입
//    @PostMapping("/user/student")
//    public ResponseEntity<SuccessResponseDto> signupStudent(
//        @RequestBody @Valid SaveStudentRequest studentRequestDto
//    ) {
//        userService.signup(studentRequestDto);
//        return ResponseEntity.ok(new SuccessResponseDto<Map>(
//            200, "회원 가입에  성공했습니다.", new HashMap()
//        ));
//    }
//
//    // 선생 회원가입
//    @PostMapping("/user/teacher")
//    public ResponseEntity<SuccessResponseDto> signupTeacher(
//        @RequestBody @Valid UserDto.SaveTeacherRequest teacherRequestDto) {
//        userService.signup(teacherRequestDto);
//        return ResponseEntity.ok(new SuccessResponseDto<Map>(
//            200, "회원 가입에 성공했습니다.", new HashMap()
//        ));
//    }

    @PostMapping("/user")
    public ResponseEntity<SuccessResponseDto> signup(
        @RequestBody @Valid UserDto.SaveUserRequest userRequestDto) {
        userService.signup(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new SuccessResponseDto<Map>(201, "회원 가입에 성공했습니다.", new HashMap()));
    }


    // 사용자 회원 정보 조회
    @GetMapping("/user/{req}")
    @PreAuthorize("hasAnyRole('PTTEACHER','PTSTUDENT')")
    public ResponseEntity<SuccessResponseDto> getUserInfo(
        @AuthenticationPrincipal UserBase user,
        @PathVariable @NotEmpty List<String>  req) { // 어차피 입력하지 않으면 post로 받는 것 같다.
        return ResponseEntity.ok(new SuccessResponseDto<Map>(
            200, "회원 정보를 불러오는데 성공했습니다", userService.getUserInfo(user.getEmail(), req)
        ));
    }
}
