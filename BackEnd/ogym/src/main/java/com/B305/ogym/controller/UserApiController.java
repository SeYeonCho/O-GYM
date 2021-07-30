package com.B305.ogym.controller;

import static com.B305.ogym.controller.dto.UserDto.SaveStudentRequest;
import static com.B305.ogym.controller.dto.UserDto.SaveUserRequest;

import com.B305.ogym.controller.dto.SuccessResponseDto;
import com.B305.ogym.controller.dto.UserDto;
import com.B305.ogym.domain.users.common.UserBase;
import com.B305.ogym.service.UserService;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponseDto> signup(
        @RequestBody @Valid SaveUserRequest signupRequest
    ) {
        userService.signup(signupRequest);
        return ResponseEntity.ok(new SuccessResponseDto<Map>(
            200, "회원가입이 성공했습니다", new HashMap()
        ));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('PTTEACHER','ADMIN','USER')")
    public ResponseEntity<SuccessResponseDto> getMyInfo() {
        UserBase user = null;
        user = userService.getMyUserWithAuthorities();
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("id", user.getId());
        map.put("role", user.getAuthority());
        map.put("email", user.getEmail());
        return ResponseEntity.ok(new SuccessResponseDto<Map>(

            200, "회원 정보를 불러오는데 성공했습니다", map
        ));
    }

    @DeleteMapping("/user")
    @PreAuthorize("hasAnyRole('PTTEACHER','ADMIN','USER')")
    public ResponseEntity<SuccessResponseDto> deleteMyUser() {
        userService.deleteUserBase();
        // Security Context에서도 지워야한다.
        return ResponseEntity.ok(new SuccessResponseDto<Map>(
            200, "회원정보 삭제에 성공했습니다", new HashMap()
        ));
    }

    @PostMapping("/user/student")
    public ResponseEntity<SuccessResponseDto> signupStudent(
        @RequestBody @Valid SaveStudentRequest studentRequestDto
    ) {
        userService.signup(studentRequestDto);
        return ResponseEntity.ok(new SuccessResponseDto<Map>(
            200, "회원 가입에  성공했습니다.", new HashMap()
        ));
    }

    @PostMapping("/user/teacher")
    public ResponseEntity<SuccessResponseDto> signupTeacher(
        @RequestBody @Valid UserDto.SaveTeacherRequest teacherRequestDto) {
        userService.signup(teacherRequestDto);
        return ResponseEntity.ok(new SuccessResponseDto<Map>(
            200, "회원정보 수정에 성공했습니다.", new HashMap()
        ));
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserBase> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
    }
}
