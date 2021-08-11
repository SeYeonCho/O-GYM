package com.B305.ogym.controller;

import com.B305.ogym.controller.dto.AuthDto;
import com.B305.ogym.controller.dto.AuthDto.TokenDto;
import com.B305.ogym.controller.dto.SuccessResponseDto;
import com.B305.ogym.service.AuthService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/authenticate")
    public ResponseEntity<SuccessResponseDto> authorize(@RequestBody @Valid AuthDto.LoginDto loginDto) {
        TokenDto tokenDto = authService.authorize(loginDto.getEmail(), loginDto.getPassword());
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDto.getAccesstoken());
        return new ResponseEntity(
            new SuccessResponseDto<TokenDto>(200, "로그인에 성공했습니다.", tokenDto),
            HttpStatus.OK);
    }


    @PostMapping("/reissue")
    public ResponseEntity<SuccessResponseDto> reissue(@RequestBody @Valid TokenDto requestTokenDto) {

        TokenDto tokenDto = authService.reissue(requestTokenDto.getAccessToken(),requestTokenDto.getRefreshToken());

        return new ResponseEntity(
            new SuccessResponseDto<TokenDto>(200, "재발급에 성공했습니다.", tokenDto),
            HttpStatus.OK);
    }
}
