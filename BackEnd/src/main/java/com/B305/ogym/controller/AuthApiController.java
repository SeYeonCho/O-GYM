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
    /*
     * 로그인을 했을때 토큰(AccessToken, RefreshToken)을 주는 메서드
     */
    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(
        @RequestBody @Valid AuthDto.LoginDto loginDto) {
        TokenDto tokenDto = authService.authorize(loginDto.getEmail(), loginDto.getPassword());
        return ResponseEntity.ok(tokenDto);
    }

    /*
     * AccessToken이 만료되었을 때 토큰(AccessToken , RefreshToken)재발급해주는 메서드
     */
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(
        @RequestBody @Valid TokenDto requestTokenDto) {

        TokenDto tokenDto = authService
            .reissue(requestTokenDto.getAccessToken(), requestTokenDto.getRefreshToken());

        return ResponseEntity.ok(tokenDto);
    }
}
