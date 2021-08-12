package com.B305.ogym.service;

import com.B305.ogym.common.jwt.TokenProvider;
import com.B305.ogym.common.util.RedisUtil;
import com.B305.ogym.controller.dto.AuthDto.TokenDto;
import com.B305.ogym.domain.users.common.UserBase;
import com.B305.ogym.exception.user.UnauthorizedException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisUtil redisUtil;

    @Transactional
    public TokenDto authorize(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(email, password); // 인증 전 객체

        Authentication authentication = authenticationManagerBuilder.getObject()
            .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String authorities = getAuthorities(authentication);
        TokenDto tokenDto = tokenProvider.createToken(authentication.getName(), authorities);

        redisUtil.set(email, tokenDto.getRefreshToken(), 60 * 24 * 7); // 7일동안 refreshtoken 저장
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(String requestAccessToken, String requestRefreshToken) {
        // Refresh 토큰 검증
        if (!tokenProvider.validateToken(requestRefreshToken)) {
            throw new UnauthorizedException("유효하지 않은 RefreshToken 입니다");
        }
        // Access Token
        Authentication authentication = tokenProvider.getAuthentication(requestAccessToken);

        // Email에 해당하는 refreshToken이 존재하는지 체크 (로그아웃 OR 회원탈퇴? -> 회원탈퇴 수정)
        UserBase principal = (UserBase) authentication.getPrincipal();
//        RefreshToken refreshToken = refreshTokenRepository.findById(authentication.getName()) // 헷갈리네

        if (!redisUtil.hasKey(principal.getEmail())) {
            throw new UnauthorizedException("인증되지 않은 유저입니다");
        }

        // 4. Refresh Token 일치하는지 검사
        if (!redisUtil.get(principal.getEmail()).equals(requestRefreshToken)) {
            throw new RuntimeException("토큰이 일치하지 않습니다.");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String authorities = getAuthorities(authentication);

        TokenDto tokenDto = tokenProvider.createToken(principal.getEmail(), authorities);

        redisUtil.set(principal.getEmail(), tokenDto.getRefreshToken(), 60 * 24 * 7);

        return tokenDto;
    }

    public String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
    }
}
